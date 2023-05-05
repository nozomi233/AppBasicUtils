//package com.zhulang.basic.utils.rpc;
//
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.MDC;
//import org.springframework.boot.context.event.ApplicationStartedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
///**
// * @Author zhulang
// * @Date 2023-05-05
// **/
//@Slf4j
//@Component
//public class FutureUtils implements ApplicationListener<ApplicationStartedEvent> {
//    private static ExecutorService executorService = new ThreadPoolExecutor(80, 500, 5, TimeUnit.MINUTES,
//            new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
//
//    private static Map<Class, FutureInvoker> futureInvokerMap = new ConcurrentHashMap<>();
//
//    public static <T> FutureInvoker<T> futureInvoke(Class<T> clazz) {
//        FutureInvoker<T> futureInvoker = futureInvokerMap.get(clazz);
//        if (futureInvoker != null) {
//            return futureInvoker;
//        }
//
//        return instanceFutureInvoker(clazz);
//    }
//
//
//    public static <T> Future<T> futureInvoke(Callable<T> callable) {
//        return executorService.submit(callable);
//    }
//
//    @SneakyThrows(value = Exception.class)
//    public static <T> T get(Future<T> future) {
//        return future.get();
//    }
//
//    @SneakyThrows(value = Throwable.class)
//    public static <T> T get(Future<T> future, long timeout, TimeUnit unit) {
//        return future.get(timeout, unit);
//    }
//
//
//    private static synchronized <T> FutureInvoker<T> instanceFutureInvoker(Class<T> clazz) {
//        FutureInvoker<T> futureInvoker = futureInvokerMap.get(clazz);
//        if (futureInvoker != null) {
//            return futureInvoker;
//        }
//
//        futureInvoker = GlobalSoaClientContainer.getGlobalContainer().newBuilder(clazz).buildAsFutureInvoker();
//        futureInvokerMap.put(clazz, futureInvoker);
//
//        return futureInvoker;
//    }
//
//
//
//
//    @Override
//    public void onApplicationEvent(ApplicationStartedEvent event) {
//
//        GlobalSoaClientContainer.getGlobalContainer()
//                .registerGlobalFilter(new ClientFilter() {
//
//                    @Override
//                    public SoaResponse invoke(Invoker invoker, SoaRequest request) {
//                        return invoker.invoke(request);
//                    }
//
//                    @Override
//                    public void onAsyncResponse(SoaRequest request, SoaResponse response) throws RpcException {
//                        MDC.put(Constant.MDC_TITLE_ID, MDC.get("parentSpanId"));
//                        String resultStr = JacksonUtils.toJsonString(response.getResponse());
//                        String service = request.getInterfaceName() + "." + request.getMethodName();
//                        if (resultStr.length() < 1024 * 3) {
//                            log.info("async invoke remote service {} param = {} response = {}", service, JacksonUtils.toJsonString(request.getMethodArgs()), resultStr);
//                        } else {
//                            log.info("async invoke remote service {},param = {}", service, JacksonUtils.toJsonString(request.getMethodArgs()));
//                            List<String> strs = Splitter.fixedLength(1024 * 3).splitToList(resultStr);
//                            for (int i = 0; i < strs.size() && i < 3; i++) {
//                                log.info("async {},resultIndex={},result={}", service, i, strs.get(i));
//                            }
//                        }
//
//                        if (response.isError()) {
//                            log.error("async invoke error {}", service, response.getException());
//                        }
//                        MDC.remove(Constant.MDC_TITLE_ID);
//                    }
//                });
//    }
//
//
//}
