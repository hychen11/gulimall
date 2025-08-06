package com.hychen11.cart.service;

import com.hychen11.cart.vo.Cart;
import com.hychen11.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: CartService
 * @date ：2025/7/31 21:57
 */
public interface CartService {
    Cart getCart() throws ExecutionException, InterruptedException;

    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    void clearCart(String cartKey);

    void checkItem(Long skuId, Integer check);

    void countItem(Long skuId, Integer num);

    void deleteItem(Long skuId);

    List<CartItem> getCurrentUserCartItems();
}
