(ns discount-alerter.core
  (:gen-class)
  (:require [org.httpkit.client :as http]
            [net.cgrand.enlive-html :as html]))

(def base-url "http://store.steampowered.com/app/")

(def game-id-list [211420 203770])

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn fetch-url-sync [url]
  (let [{:keys [status headers body error] :as resp} @(http/get url)]
    (when-not error
      (html/html-resource (java.io.StringReader. body)))))

(defn fetch-urls-async [urls]
  (let [futures (doall (map http/get urls))]
    (for [resp futures
          :let [{:keys [body error] :as r} @resp]
          :when (not error)]
      (html/html-resource (java.io.StringReader. body)))))

(defn get-name [pg]
  (first (:content (first (html/select pg [:div.apphub_AppName])))))

(defn discounted? [pg]
  (not (empty? (html/select pg [:div.game_area_purchase_game_wrapper 
                                :div.game_purchase_action 
                                :div.discount_final_price]))))

(defn get-game [id]
  (let [pg (fetch-url-sync (str base-url id))
        name (get-name pg)
        discounted? (discounted? pg)]
    {:id id :name name :discounted? discounted?}))

(defn get-games [pages]
  (for [pg (fetch-urls-async pages)
        :let [name (get-name pg)
              discounted? (discounted? pg)]]
    {:name name :discounted? discounted?}))

(defn discount-report [g]
  (mapv get-game g))

(defn discount-reports [g]
  (get-games (mapv #(str base-url %) g)))

(defn -main
  "Get discount reports of games in argument list"
  [& args]
  (prn (discount-report args)))
