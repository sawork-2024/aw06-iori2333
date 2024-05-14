[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/IMEm063v)

# Micro WebPoS

请参考 spring-petclinic-rest/spring-petclinic-microserivces 将 webpos 项目改为微服务架构，具体要求包括：

1. 至少包含独立的产品管理服务、订单管理服务以及 discovery/gateway 等微服务架构下需要的基础设施服务；
2. 请将系统内的不同微服务实现不同的计算复杂度，通过压力测试实验验证对单个微服务进行水平扩展（而无需整个系统所有服务都进行水平扩展）可以提升系统性能，请给出实验报告；
3. 请使用`RestTemplate`进行服务间访问，验证 Client-side LB 可行；
4. 请注意使用断路器等机制；
5. 如有兴趣可在 kubernetes 或者 minikube 上进行部署。

请编写 readme 对自己的系统和实验进行详细介绍。

# 微服务架构的 MicroPos

## 项目结构

我们将整个 MicroPos 项目共分为 5 部分：

- `pos-apis`：用于定义各个微服务之间的通信接口，并通过`OpenAPI`自动生成对应的代码以及文档。
- `pos-products`：对前几次作业的服务进行了重构，将其中的产品部分单独提取出来并进行了微服务化。
- `pos-carts`：购物车服务，用于管理用户的购物车信息以及订单信息。
- `pos-discovery`：服务发现与注册中心，用于管理各个微服务的注册与发现。
- `pos-gateway`：网关服务，用于对外提供统一的访问入口。

其中，`pos-products`, `pos-carts`，`pos-gateway`均通过`Eukera`进行注册。

## 服务间通信

为了验证客户端 Load Balance 的可行性，我们在`pos-carts`中通过`RestTemplate`调用`pos-products`的服务，部分代码如下：

```java
private double priceOf(String productId) {
    String productURL = "http://pos-products/api/product";
    var product = restTemplate.getForObject(productURL + "/" + productId, ProductDto.class);
    if (product != null) {
        return product.getPrice();
    }
    throw new RuntimeException("Product not found: " + productId);
}
```

具体地，由于我们不维护购物车里产品的完整信息，因此在结算时，我们需要在`pos-carts`中调用`pos-products`的服务，获取对应商品的价格。

由于我们向 Discovery 服务器注册了 Products 服务端，因此在请求时会自动地进行负载均衡，并且在 Products 服务端进行了水平扩展，因此在高并发时，我们可以看到系统的性能有所提升。

## 扩展实验

Products 服务在我们的系统中具有较高的运算复杂度，因为：

1. Products 本身需要处理来自用户请求中的商品信息，并且需要从数据库中查询商品的详细信息。
2. Cart 服务结算时，需要调用 Products 服务获取商品的价格。

因此，我们对系统中的 Products 服务进行了扩展实验。我们分别设置实例数为 1-3，并通过 Gatling 进行高负载压力测试：

```scala
private val httpProtocol = http
  .baseUrl("http://localhost:8080")
  .acceptHeader("application/json")
  .doNotTrackHeader("1")
  .acceptLanguageHeader("en-US,en;q=0.5")
  .acceptEncodingHeader("gzip, deflate")
  .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

private val scn = scenario("Testing Single Server")
  .exec(http("product_request").get("/api/product"))
  .exec(http("cart_add").post("/api/cart/1"))
  .exec(http("cart_request").get("/api/cart"))
  .exec(http("checkout").post("/api/cart"))

setUp(scn.inject(atOnceUsers(1000)).protocols(httpProtocol))
```

该脚本模拟了用户的购物流程，包括：

1. 查询可用的商品列表
2. 选择商品加入购物车
3. 查看购物车中的商品有哪些
4. 结算购物车

模拟结果如下：

| 实例 | 平均响应时间 (ms) | 成功率             |
| ---- | ----------------- | ------------------ |
| 1    | 2462              | 84.9% (3396/4000)  |
| 2    | 1533              | 98.8% (3952/4000)  |
| 3    | 1432              | 100.0% (4000/4000) |

结论：

- 在实例数 1 时，出现了部分请求丢失的情况。经排查推测是因为部分经过 RestTemplate 的请求没能正确的得到结果，而是触发了断路器，故造成了大量请求失败。
- 随着实例数增大，系统响应时间和成功率出现较为明显的改善，因此横向扩展系统中复杂度较高的部分可以提升性能。
- 横向扩展有利于提升系统的性能，但是并不是线性的。随着实例数量增大，当复杂度不再是系统的瓶颈时，横向扩展的效果会逐渐减弱。
