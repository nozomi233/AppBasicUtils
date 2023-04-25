package com.zhulang.common.common.consts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
public class Constants {
    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(32, 32, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.DiscardPolicy());

    public static final int DEFAULT_BIZ_ERR_CODE = 800;

    public static final int DEFAULT_DB_ERR_CODE = 700;

    public static final int DEFAULT_RPC_ERR_CODE = 900;

    public static final int DEFAULT_POOL_ERR_CODE = 600;

    public static final int DEFAULT_ERR_CODE = 500;

    public static final int DEFAULT_PROCESS_ERR_CODE = 1000;

    public static final String SET_TEST = "http://%s-set.hellobike.cn/#/m-a/trace/invoke?currentTab=soaLog&idType=traceId&traceid=%s&logTime=%s&timeRegion=1m";

    public static final String SET_PRO = "http://set.hellobike.cn/#/m-a/trace/invoke?currentTab=soaLog&idType=traceId&traceid=%s&logTime=%s&timeRegion=1m";

    public static final String ES_FAT = "https://fat-es.hellobike.cn/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-30m,to:now))&_a=(columns:!(_source),filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:e8a24590-9d2d-11eb-abb3-0347c542c13b),query:(match_phrase:(reqid:%s)))),index:e8a24590-9d2d-11eb-abb3-0347c542c13b,interval:auto,query:(language:lucene,query:''),sort:!())";

    public static final String ES_PRO = "https://es.hellobike.cn/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(columns:!(_source),filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:'8e583740-8561-11eb-8401-29af0a5fd26e'),query:(match_phrase:(reqid:%s)))),index:'8e583740-8561-11eb-8401-29af0a5fd26e',interval:auto,query:(language:kuery,query:''),sort:!())";

    public static final String ES_UAT = "https://fat-es.hellobike.cn/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-30m,to:now))&_a=(columns:!(_source),filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ee3ff470-9d2d-11eb-abb3-0347c542c13b),query:(match_phrase:(reqid:%s)))),index:ee3ff470-9d2d-11eb-abb3-0347c542c13b,interval:auto,query:(language:lucene,query:''),sort:!())";

    public static final String ES_PRE = "https://es.hellobike.cn/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(columns:!(_source),filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:'28ceb200-8bdd-11eb-b643-eb3dce05866f'),query:(match_phrase:(reqid:%s)))),index:'28ceb200-8bdd-11eb-b643-eb3dce05866f',interval:auto,query:(language:kuery,query:''),sort:!())";

    public static final String FAT_HLOG = "http://fat-hlog.hellobike.cn/logSearch?appId=%s&startTime=%s&endTime=%s&levels=&title=&tags=&content=&machines=&traceId=%s&logName=";

    public static final String UAT_HLOG = "http://uat-hlog.hellobike.cn/logSearch?appId=%s&startTime=%s&endTime=%s&levels=&title=&tags=&content=&machines=&traceId=%s&logName=";

    public static final String PRE_HLOG = "http://pre-hlog.hellobike.cn/logSearch?appId=%s&startTime=%s&endTime=%s&levels=&title=&tags=&content=&machines=&traceId=%s&logName=";

    public static final String PRO_HLOG = "http://hlog.hellobike.cn/logSearch?appId=%s&startTime=%s&endTime=%s&levels=&title=&tags=&content=&machines=&traceId=%s&logName=";

    public static final String MDC_TITLE_ID = "title";

    public static final String TRACE_ID = "traceId";

    public static final String SPAN_ID = "spanId";

    public static final String REQ_ID = "reqId";

}
