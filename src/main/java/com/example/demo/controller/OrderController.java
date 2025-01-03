package com.example.demo.controller;

import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.enums.StatusOrder;
import com.example.demo.service.OrderService;
import com.example.demo.utils.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Validated
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final OrderService orderService;
    @PostMapping("/")
    public ResponseData<Long> addOrder(@Valid @RequestBody OrderRequest request){
        try{
            long orderId = orderService.saveOrder(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "created order successfully !",orderId);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
    @GetMapping("/{id}")
    public ResponseData<OrderResponse> getOrderById(@PathVariable (name = "id") @Min(1) int id){
        try{
            OrderResponse response = orderService.getOrder(id);
            return new ResponseData<>(HttpStatus.OK.value(),"Get order successfully ! ",response);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteOrderById(@PathVariable @Min(1) int id ){
        try{
            orderService.deleteOrderById(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete order successfully !");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseData<Void> updateOrder(@PathVariable @Min(1) int id,@Valid @RequestBody OrderRequest request) {
        try{
            orderService.updateOrder(request,id);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update order successfully !");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseData<Void> changeStatusOrder(@PathVariable @Min(1) int id, @RequestParam(name = "status") StatusOrder status){
        try{
            orderService.changeStatusOrder(id,status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update status order successfully !");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseData<PageResponse<List<OrderResponse>>> getOrders(@RequestParam(name = "page_no", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)  @Min(0) int pageNo,
                                                               @RequestParam(name = "page_size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) @Min(1) int pageSize){
        return new ResponseData<>(HttpStatus.OK.value(), "get list order successfully!",orderService.getOrders(pageNo,pageSize));
    }
    @GetMapping("/list-by-status")
    public ResponseData<PageResponse<List<OrderResponse>>> getOrdersByStatus(@RequestParam(name = "status") StatusOrder status,
                                                                            @RequestParam(name = "page_no", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)  @Min(0) int pageNo,
                                                                            @RequestParam(name = "page_size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) @Min(1) int pageSize){
        return new ResponseData<>(HttpStatus.OK.value(), "get list order by status successfully!",orderService.getOrdersByStatus(status,pageNo,pageSize));

    }

//    @GetMapping("/list-multiplecolumn")
//    public ResponseData<PageResponse<List<OrderResponse>>> getOrdersSortByMultipleColumn(@RequestParam(name = "page_no", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)  @Min(0) int pageNo,
//                                                                                     @RequestParam(name = "page_size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) @Min(1) int pageSize,
//                                                                                     @RequestParam(required = false) String... sorts){
//
//    }

    @GetMapping("/list-advanced")
    public ResponseData<PageResponse<List<OrderResponse>>> getOrdersAdvanced(@RequestParam Map<String,Object> params){


        return new ResponseData<>(HttpStatus.OK.value(), "get list order advanced successfully!",orderService.getOrdersAdvanced(params));
    }
    @GetMapping("/advanced-search-by-crietia")
    public ResponseData<PageResponse<List<OrderResponse>>> getOrdersAdvancedByCrietia(@RequestParam(name = "page_no", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)  @Min(0) int pageNo,
                                                                                      @RequestParam(name = "page_size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) @Min(1) int pageSize,
                                                                                      @RequestParam String... params) {
        return new ResponseData<>(HttpStatus.OK.value(), "get list order advanced successfully!",orderService.getOrdersAdvancedByCrietia(pageNo,pageSize,params));

    }
}
