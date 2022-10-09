package com.andc.amway.datacubecatcher.wx.message;

/**
 * 接收消息中MsgType为Event下的类型
 */
public enum WxMessageEvent {
    subscribe,                          //关注
    unsubscribe,                        //取消关注
    LOCATION,                           //上报地理位置
    CLICK,                              //自定义菜单拉取消息
    VIEW,                               //点击菜单跳转
    SCAN,                               //扫描带参数二维码
    scancode_push,                      //扫码推事件
    scancode_waitmsg,                   //扫描显示消息接受中
    pic_sysphoto,                       //弹出系统拍照发图
    pic_photo_or_album,                 //弹出拍照或者相册发图
    pic_weixin,                         //弹出微信相册发图器
    location_select,                    //弹出地理位置选择器
    TEMPLATESENDJOBFINISH,              //模板消息送达情况提醒
    MASSSENDJOBFINISH,                  //群发消息后的通知
    merchant_order,                     //微信小店订单支付后的通知
    qualification_verify_success,       //资质认证成功
    qualification_verify_fail,          //资质认证失败
    naming_verify_success,              //名称认证成功
    naming_verify_fail,                 //名称认证成功
    annual_renew,                       //年审通知
    verify_expired                      //认证过期失效通知
}
