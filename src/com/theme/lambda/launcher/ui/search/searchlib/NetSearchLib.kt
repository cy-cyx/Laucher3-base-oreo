package com.theme.lambda.launcher.ui.search.searchlib

// 网址搜索库
object NetSearchLib {

    // 通过关键字找出5条相似网址
    fun findNetUrl(s: String): ArrayList<String> {
        val result = ArrayList<String>()
        for (it in netUrls) {
            if (it.contains(s)) {
                result.add(it)
                if (result.size >= 5) {
                    return result
                }
            }
        }
        return result
    }

    private val netUrls = arrayListOf(
        "https://www.google.com/",
        "https://www.bing.com/",
        "https://www.yahoo.com/",
        "https://duckduckgo.com/",
        "https://www.facebook.com/",
        "https://www.twitter.com/",
        "https://www.instagram.com/",
        "https://www.linkedin.com/",
        "https://www.snapchat.com/",
        "https://www.reddit.com/",
        "https://www.tiktok.com/",
        "https://www.whatsapp.com/",
        "https://www.amazon.com/",
        "https://www.ebay.com/",
        "https://www.aliexpress.com/",
        "https://www.walmart.com/",
        "https://www.bestbuy.com/",
        "https://www.target.com/",
        "https://www.bbc.com/",
        "https://www.cnn.com/",
        "https://www.nytimes.com/",
        "https://www.theguardian.com/",
        "https://www.reuters.com/",
        "https://www.aljazeera.com/",
        "https://www.youtube.com/",
        "https://www.netflix.com/",
        "https://www.vimeo.com/",
        "https://www.hulu.com/",
        "https://www.twitch.tv/",
        "https://www.coursera.org/",
        "https://www.udemy.com/",
        "https://www.edx.org/",
        "https://www.khanacademy.org/",
        "https://www.duolingo.com/",
        "https://www.linkedin.com/learning",
        "https://www.github.com/",
        "https://www.stackoverflow.com/",
        "https://developer.mozilla.org/",
        "https://www.w3schools.com/",
        "https://codepen.io/",
        "https://drive.google.com/",
        "https://www.dropbox.com/",
        "https://onedrive.live.com/",
        "https://www.box.com/",
        "https://www.icloud.com/",
        "https://www.expedia.com/",
        "https://www.airbnb.com/",
        "https://www.uber.com/",
        "https://www.booking.com/",
        "https://www.tripadvisor.com/",
        "https://www.bloomberg.com/",
        "https://finance.yahoo.com/",
        "https://www.investing.com/",
        "https://www.marketwatch.com/",
        "https://www.cnbc.com/",
        "https://www.webmd.com/",
        "https://www.healthline.com/",
        "https://www.myfitnesspal.com/",
        "https://www.fitbit.com/",
        "https://www.canva.com/",
        "https://www.adobe.com/",
        "https://www.dribbble.com/",
        "https://www.behance.net/",
        "https://www.twitch.tv/",
        "https://www.gog.com/",
        "https://www.paypal.com/",
        "https://www.venmo.com/",
        "https://www.stripe.com/",
        "https://www.wise.com/",
        "https://www.medium.com/",
        "https://www.quora.com/",
        "https://www.slideshare.net/",
        "https://www.techcrunch.com/",
        "https://www.wired.com/",
        "https://www.theverge.com/",
        "https://www.etsy.com/",
        "https://www.craigslist.org/",
        "https://scholar.google.com/",
        "https://www.jstor.org/",
        "https://www.ifttt.com/",
        "https://www.zapier.com/",
        "https://www.wikipedia.org/",
        "https://www.imdb.com/",
        "https://archive.org/"
    )
}