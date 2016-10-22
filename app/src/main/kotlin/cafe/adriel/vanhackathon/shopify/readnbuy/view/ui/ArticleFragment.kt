package cafe.adriel.vanhackathon.shopify.readnbuy.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import cafe.adriel.vanhackathon.shopify.readnbuy.Constant
import cafe.adriel.vanhackathon.shopify.readnbuy.R
import cafe.adriel.vanhackathon.shopify.readnbuy.model.entity.Article
import cafe.adriel.vanhackathon.shopify.readnbuy.model.entity.Product
import cafe.adriel.vanhackathon.shopify.readnbuy.presenter.ArticlePresenter
import cafe.adriel.vanhackathon.shopify.readnbuy.presenter.IArticlePresenter
import cafe.adriel.vanhackathon.shopify.readnbuy.util.prettyDate
import cafe.adriel.vanhackathon.shopify.readnbuy.view.IArticleView
import cafe.adriel.voxrecorder.view.ui.base.BaseFragment
import com.pawegio.kandroid.e
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.view.*

class ArticleFragment : BaseFragment(), IArticleView {

    companion object {

        fun newInstance(article: Article) : ArticleFragment {
            val args = Bundle()
            args.putParcelable(Constant.EXTRA_ARTICLE, article)

            val fragment = ArticleFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var presenter : IArticlePresenter
    private lateinit var article : Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        article = arguments.getParcelable(Constant.EXTRA_ARTICLE)
        presenter = ArticlePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_article, container, false)
        view.vBody.let {
            it.settings.defaultFontSize = 20
            it.settings.javaScriptEnabled = true
            it.setWebViewClient(object : WebViewClient(){
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        presenter.loadProduct(it)
                    }
                    return true
                }
            })
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showArticle(article)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vBody?.destroy()
    }

    override fun showArticle(article: Article){
        val html = """
            |<!doctype html>
            |<html lang="en">
            |<head>
            |    <meta charset="utf-8">
            |    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
            |    <link type="text/css" rel="stylesheet" href="css/normalize.css">
            |    <link type="text/css" rel="stylesheet" href="css/article.css">
            |</head>
            |<body>
            |   <article>
            |       ${article.body}
            |   </article>
            |
            |   <script type="text/javascript" src="js/zepto.js"></script>
            |   <script type="text/javascript" src="js/article.js"></script>
            |</body>
            |</html>
            """.trimMargin()
        vTitle.text = article.title
        vDate.text = article.date.prettyDate()
        vBody.loadDataWithBaseURL("file:///android_asset/article/", html, "text/html", "utf-8", null)
    }

    override fun showProduct(product: Product){
        e { "SHOW PRODUCT $product" }
    }

}