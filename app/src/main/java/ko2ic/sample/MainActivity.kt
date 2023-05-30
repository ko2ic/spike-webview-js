package ko2ic.sample

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    private val mutableList:MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main);

        // WebViewの初期化
        webView = findViewById(R.id.webview);


        // JavaScriptの有効化
        webView.settings.javaScriptEnabled = true

        // WebViewのクライアントを設定
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // ページの読み込みが完了した後にJavaScriptを実行する
//                view.loadUrl("javascript:/*ここに実行したいJavaScriptコードを記述*/")
                view.evaluateJavascript(
"""
(function() {
    window.addEventListener('scroll', function() {  
        var scrollTop = window.scrollY;        
        var viewportWidth = window.innerWidth;
        var x = viewportWidth / 2;
        var y = scrollTop;   

        var elements = document.querySelector('body').querySelectorAll('[data-marked="false"]');                              
        elements.forEach(function(element) {             
            var topElement = document.elementFromPoint(x, 100);
console.log(topElement);
console.log(element);            
            if (topElement !== element) {
                return;
            }
            element.childNodes.forEach(function(child) {                    
                if (child.nodeType === Node.TEXT_NODE) {
                    // TODO ここでAPI呼び出して翻訳する
                    element.textContent = element.textContent.replace(/a/g, 'あ');  
                }                  
                // マークをつける
                element.setAttribute('data-marked', 'true');                                           
            });                                                                                                                 
        });    
  });

  function traverse(element) {
    if (element.nodeType === Node.TEXT_NODE) {      
      element.parentElement.setAttribute('data-marked', 'false');
    } else {
      const children = element.childNodes;
      for (let i = 0; i < children.length; i++) {
        traverse(children[i]);
      }
    }
  }

  var elements = document.querySelector('body').querySelectorAll(':not([data-marked])');
  elements.forEach(function(element) {
    traverse(element);  
  });          
})();    
""".trimIndent()
                ){ _ ->
                }
            }
        }
        // 表示するウェブページを読み込む
        webView.loadUrl("https://www.cnbc.com/amp/2023/04/30/the-big-cyber-risks-when-chatgpt-and-ai-are-secretly-used-by-employees.html")
    }
}
