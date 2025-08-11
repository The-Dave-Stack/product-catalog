package com.thedavestack.productcatalog.resource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.thedavestack.productcatalog.dto.BatchCreateProductRequest;
import com.thedavestack.productcatalog.dto.CreateProductRequest;
import com.thedavestack.productcatalog.dto.ProductListResponse;
import com.thedavestack.productcatalog.dto.ProductResponse;
import com.thedavestack.productcatalog.model.Product;
import com.thedavestack.productcatalog.service.ProductService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/products")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject private ProductService productService;

    @POST
    public CompletionStage<Response> createProduct(@Valid CreateProductRequest request) {
        Product product = Product.fromRequest(request);

        return productService
                .createProduct(product)
                .thenApply(this::toProductResponse)
                .thenApply(
                        response ->
                                Response.status(Response.Status.CREATED).entity(response).build())
                .exceptionally(this::handleException);
    }

    @POST
    @Path("/batch-create")
    public CompletionStage<Response> createProductsBatch(@Valid BatchCreateProductRequest request) {
        List<Product> products = request.products().stream().map(Product::fromRequest).toList();

        return productService
                .createProductsBatch(products)
                .thenApply(this::toProductResponseList)
                .thenApply(
                        responses ->
                                Response.status(Response.Status.CREATED).entity(responses).build())
                .exceptionally(this::handleException);
    }

    @GET
    @Path("/{id}")
    public CompletionStage<Response> getProductById(@PathParam("id") String id) {
        return productService
                .getProductById(id)
                .thenApply(this::toProductResponse)
                .thenApply(response -> Response.ok(response).build())
                .exceptionally(this::handleException);
    }

    @GET
    public CompletionStage<Response> getProducts(
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) @Max(100) int size,
            @QueryParam("category") String category) {

        int offset = page * size;

        CompletionStage<List<Product>> productsFuture;
        CompletionStage<Long> countFuture;

        if (category != null && !category.trim().isEmpty()) {
            productsFuture = productService.getProductsByCategory(category, offset, size);
            countFuture = productService.countProductsByCategory(category);
        } else {
            productsFuture = productService.getAllProducts(offset, size);
            countFuture = productService.countProducts();
        }

        return productsFuture
                .thenCombine(
                        countFuture,
                        (products, totalElements) -> {
                            List<ProductResponse> productResponses =
                                    toProductResponseList(products);
                            int totalPages = (int) Math.ceil((double) totalElements / size);

                            ProductListResponse response =
                                    new ProductListResponse(
                                            productResponses,
                                            page,
                                            size,
                                            totalElements,
                                            totalPages);

                            return Response.ok(response).build();
                        })
                .exceptionally(this::handleException);
    }

    @PUT
    @Path("/{id}")
    public CompletionStage<Response> updateProduct(
            @PathParam("id") String id, @Valid CreateProductRequest request) {
        Product product = Product.fromRequestForUpdate(id, request);

        return productService
                .updateProduct(product)
                .thenApply(this::toProductResponse)
                .thenApply(response -> Response.ok(response).build())
                .exceptionally(this::handleException);
    }

    @DELETE
    @Path("/{id}")
    public CompletionStage<Response> deleteProduct(@PathParam("id") String id) {
        return productService
                .deleteProduct(id)
                .thenApply(deleted -> Response.noContent().build())
                .exceptionally(this::handleException);
    }

    @GET
    @Path("/export")
    @Produces("application/json")
    public CompletionStage<Response> exportProducts(
            @QueryParam("format") @DefaultValue("json") String format) {
        if (!"json".equalsIgnoreCase(format)) {
            return CompletableFuture.completedStage(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"Only JSON format is currently supported\"}")
                            .build());
        }

        return productService
                .getAllProducts(0, Integer.MAX_VALUE)
                .thenApply(this::toProductResponseList)
                .thenApply(
                        products ->
                                Response.ok(products)
                                        .header(
                                                "Content-Disposition",
                                                "attachment; filename=products.json")
                                        .build())
                .exceptionally(this::handleException);
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.id(),
                product.sku(),
                product.name(),
                product.description(),
                product.price(),
                product.category(),
                product.createdAt(),
                product.updatedAt());
    }

    private List<ProductResponse> toProductResponseList(List<Product> products) {
        return products.stream().map(this::toProductResponse).toList();
    }

    private Response handleException(Throwable throwable) {
        if (throwable instanceof jakarta.ws.rs.WebApplicationException webEx) {
            return webEx.getResponse();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"Internal server error\"}")
                .build();
    }
}
