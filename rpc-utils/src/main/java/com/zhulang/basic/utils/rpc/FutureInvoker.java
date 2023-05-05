package com.zhulang.basic.utils.rpc;

import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public interface FutureInvoker<T> {
    <R> Future<R> call(Consumer<T> var1);
}

