(defproject discount-alerter "0.1.0-SNAPSHOT"
  :description "Checks given games' prices for discount"
  :url "https://github.com/alisarac/discount-alerter"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.18"]
                 [enlive "1.1.5"]]
  :main ^:skip-aot discount-alerter.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
