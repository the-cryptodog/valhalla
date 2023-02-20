package com.app.valhalla.ui.payment

import android.os.Bundle
import android.util.Log
import com.android.billingclient.api.*
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity

class PaymentActivity : BaseActivity(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // 创建 BillingClient 对象
        billingClient = BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        // 启动连接
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // 查询商品信息
                    val skuList = listOf("my_product_id")
                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.INAPP)
                        .build()

//                    billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
//                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList?.isNotEmpty() ?: ) {
//                            // 商品信息查询成功，启动付款流程
//                            val flowParams = BillingFlowParams.newBuilder()
//                                .setSkuDetails(skuDetailsList[0])
//                                .build()
//                            billingClient.launchBillingFlow(this@PaymentActivity, flowParams)
//                        }
//                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d("PaymentActivity", "Billing service disconnected")
            }
        })
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        // 处理购买结果
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                // 处理每个购买项目
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    // 购买成功
                    // 在这里执行应用内购买逻辑
                } else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                    // 购买未决定
                } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    // 购买失败
                }
            }
        }
    }
}