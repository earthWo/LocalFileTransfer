package whitelife.win.socketlibrary.model;

import android.content.Intent;

import whitelife.win.socketlibrary.callback.CallbackSet;
import whitelife.win.socketlibrary.callback.OrderType;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public class ServiceOrder {

    private static final String ORDER="order";

    private static final String DATA="data";

    private static final String MID="mid";


    @OrderType
    private int order;

    private String data;

    private int mid;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    /**
     * 解析service的intent
     * @param intent
     */
    public static ServiceOrder createOrder(Intent intent){
        if(intent==null||intent.getIntExtra(ORDER,0)==0){
            return null;
        }
        ServiceOrder serviceOrder=new ServiceOrder();
        serviceOrder.setOrder(intent.getIntExtra(ORDER,0));
        serviceOrder.setData(intent.getStringExtra(DATA));
        return serviceOrder;
    }

    public static Intent createIntent(@OrderType int order,String data){

        Intent intent=new Intent();
        intent.putExtra(ORDER,order);
        intent.putExtra(DATA,data);
        intent.putExtra(MID, CallbackSet.decodeMessageId(data));
        return intent;

    }

}
