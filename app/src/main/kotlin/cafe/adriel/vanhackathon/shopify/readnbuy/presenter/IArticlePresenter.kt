package cafe.adriel.vanhackathon.shopify.readnbuy.presenter

interface IArticlePresenter {

    fun loadProduct(productUrl: String)

    fun buyProduct(productId: Long)

}