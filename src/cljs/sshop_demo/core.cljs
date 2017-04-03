(ns sshop-demo.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [sshop-demo.ajax :refer [load-interceptors!]]
            [sshop-demo.handlers]
            [sshop-demo.subscriptions])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "/"} "sshop-demo"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]
       [nav-link "#/about" "About" :about collapsed?]
       [nav-link "#/docs" "Docs" :docs collapsed?]
       [nav-link "#/admin" "Admin" :admin collapsed?]]]]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:h1 "This project is a shoes shop demo."]
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn docs-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(defn product-item
  []
  (let [editing (r/atom false)]
    (fn [{:keys [id owner name price description imgpath]}]
       [:div.block {:style {:display "inline-block"}}
        [:table
         [:tbody
         [:tr
          [:td [:img {:src (str js/context "/img/upload/thumb_" imgpath)}] ]
          ]
         [:tr
          [:td name ]
          ]
         [:tr
          [:td (.valueOf price) ]
          ]
          ]
         ]
        ]
       )))

(defn product-list []
  [:div.container
   (when-let [products @(rf/subscribe [:products])]
     [:div.row>div.col-sm-12
      [:div.table {:style {:display "table" :text-align "center" :width "100%"}}
       (for [product  products]
         ^{:key (:id product)} [product-item product])]
      ])])


(defn home-page []
    (fn []
      [:div.container
       [:div.jumbotron
        [:h1 "Welcome to Shoes Shop"]]
       [:div.row
        [:div.col-md-12

         (product-list)

         ]]]))



(defn product-item-admin
  []
  (let [editing (r/atom false)]
    (fn [{:keys [id owner name price description imgpath regdate editdate]}]
      [:div.block {:style {:display "inline-block"}}
       [:table
        [:tbody
         [:tr
          [:td [:img {:src (str js/context "/img/upload/thumb_" imgpath)}] ]
          ]
         [:tr
          [:td name ]
          ]
         [:tr
          [:td (.valueOf price) ]
          ]
         [:tr
          [:td  description ]
          ]

         [:tr
          [:td [:textarea (clojure.string/replace description #"\\r\\n|\\n|\\r" "\n" )  ] ]
          ]

         [:tr
          [:td [:textarea description ] ]
          ]

         [:tr
          [:td regdate ]
          ]
         [:tr
          [:td editdate ]
          ]
         ]
        ]
       ]
      )))

(defn product-list-admin []
  [:div.container
   (when-let [products @(rf/subscribe [:products])]
     [:div.row>div.col-sm-12
      [:div.table {:style {:display "table" :text-align "center" :width "100%"}}
       (for [product  products]
         ^{:key (:id product)} [product-item-admin product])]
      ])])

(defn admin-page []
  (fn []
    [:div.container
     [:div.row
      [:div.col-md-12

       (product-list-admin)

       ]]]))

(def pages
  {:home #'home-page
   :about #'about-page
   :docs #'docs-page
   :admin #'admin-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

(secretary/defroute "/docs" []
                    (rf/dispatch [:set-active-page :docs]))
(secretary/defroute "/admin" []
                    (rf/dispatch [:set-active-page :admin]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))


(defn get-products! []
  (GET "/products" {:handler #(rf/dispatch [:get-products %])}))


(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (get-products!)
  (hook-browser-navigation!)
  (mount-components))
