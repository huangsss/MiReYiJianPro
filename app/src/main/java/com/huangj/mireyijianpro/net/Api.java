package com.huangj.mireyijianpro.net;



import com.huangj.mireyijianpro.home.model.GankModel;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by huangasys on 2017/11/22.15:25
 * 网络请求接口;
 */

public interface Api {

    /**
     * 获取分类数据
     * 示例：http://gank.io/api/data/Android/10/1
     *
     * @param category 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param pageSize 请求个数： 数字，大于0
     * @param page     第几页：数字，大于0
     * @return
     */
    @GET("data/{category}/{pageSize}/{page}")
    Observable<GankModel> getCategoryData(@Path("category") String category,
                                          @Path("pageSize") int pageSize,
                                          @Path("page") int page);

    /**
     * 搜索
     */
    @GET("search/query/{searchkey}/category/all/count/10/page/{page}")
    Observable<GankModel> getSearchData(@Path("searchkey")String searchKey,
                                        @Path("page") int page);

}
