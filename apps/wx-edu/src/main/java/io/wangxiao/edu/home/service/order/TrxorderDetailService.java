package io.wangxiao.edu.home.service.order;

import io.wangxiao.commons.entity.PageEntity;
import io.wangxiao.edu.home.entity.order.QueryTrxorderDetail;
import io.wangxiao.edu.home.entity.order.TrxorderDetail;

import java.util.List;

/**
 * TrxorderDetail管理接口
 */
public interface TrxorderDetailService {

    /**
     * 添加TrxorderDetail
     *
     * @param trxorderDetail 要添加的TrxorderDetail
     * @return id
     */
    java.lang.Long addTrxorderDetail(TrxorderDetail trxorderDetail);

    /**
     * 批量添加TrxorderDetail
     *
     * @param List<trxorderDetail> 要添加的TrxorderDetail
     * @return id
     */
    void addBatchTrxorderDetail(List<TrxorderDetail> trxorderDetailList);

    /**
     * 根据id删除一个TrxorderDetail
     *
     * @param id 要删除的id
     */
    void deleteTrxorderDetailById(Long id);

    /**
     * 修改TrxorderDetail
     *
     * @param trxorderDetail 要修改的TrxorderDetail
     */
    void updateTrxorderDetail(TrxorderDetail trxorderDetail);

    /**
     * 根据id获取单个TrxorderDetail对象
     *
     * @param id 要查询的id
     * @return TrxorderDetail
     */
    TrxorderDetail getTrxorderDetailById(Long id);

    /**
     * 查询该用户购买过的课程
     *
     * @param trxorderDetail 查询条件
     * @return List<TrxorderDetail>
     */
    List<TrxorderDetail> getTrxorderDetailListBuy(Long userId);


    /**
     * 根据条件获取TrxorderDetail列表
     *
     * @param trxorderDetail 查询条件
     * @return List<TrxorderDetail>
     */
    List<TrxorderDetail> getTrxorderDetailList(TrxorderDetail trxorderDetail);


    /**
     * 根据条件获取TrxorderDetail 流水列表分页
     *
     * @param trxorderDetail 查询条件
     * @return List<QueryTrxorderDetail>
     */
    List<QueryTrxorderDetail> queryTrxorderDetailByOrder(QueryTrxorderDetail trxorderDetail, PageEntity page);


    /**
     * 根据流水id关联用户表查询流水详情
     *
     * @param id return QueryTrxorderDetail
     * @Date 2014-09-28
     */
    QueryTrxorderDetail queryQueryTrxorderDetailById(Long id);

    /**
     * 判断已购买直播
     *
     * @param userId
     * @param courseId
     * @return
     */
    int queryOrderByLive(Long userId, Long courseId);
}