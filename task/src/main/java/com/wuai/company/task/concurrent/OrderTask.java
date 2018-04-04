package com.wuai.company.task.concurrent;

import com.wuai.company.entity.Orders;
import com.wuai.company.enums.OrderOperateEnum;
import com.wuai.company.task.job.TaskOrderCalcBus;

/**
 * 执行的order的task
 * Created by Ness on 2017/6/6.
 */
public class OrderTask implements Task {


    public OrderTask(Orders orders, TaskOrderCalcBus taskOrderCalcBus) {

        this.orders = orders;
        this.taskOrderCalcBus = taskOrderCalcBus;
    }

    private Orders orders;
    private TaskOrderCalcBus taskOrderCalcBus;


    @Override
    public void run() {

        switch (OrderOperateEnum.getOperate(orders.getOrderOperateEnum())) {
            case ADD:
                taskOrderCalcBus.addOrders(orders);
                break;
            case DELETE:
                taskOrderCalcBus.deleteOrders(orders);
                break;
            case UPDATE:
                taskOrderCalcBus.updateOrders(orders);
                break;
            default:
                return;
        }
    }


}
