(ns sshop-demo.routes.services
  (:require [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]
            [compojure.api.sweet :refer :all]
            [compojure.api.upload :refer :all]
            [compojure.api.meta :refer [restructure-param]]
            [schema.core :as s]
            [sshop-demo.db.core :as db]
            [sshop-demo.routes.services.auth :as auth]
            [sshop-demo.routes.services.main :as main]
            [sshop-demo.routes.services.upload :as upload]
            [ring.util.http-response :refer :all]
            ))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

(s/defschema UserRegistration
  {:id                     String
   :pass                   String
   :pass-confirm           String})

(s/defschema Result
  {:result                   s/Keyword
   (s/optional-key :message) String})

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  
  (GET "/authenticated" []
       :auth-rules authenticated?
       :current-user user
       (ok {:user user}))

  (POST "/register" req
    :return Result
    :body [user UserRegistration]
    :summary "register a new user"
    (auth/register! req user))

  (POST "/login" req
    :header-params [authorization :- String]
    :summary "log in the user and create a session"
    :return Result
    (auth/login! req authorization))

  (POST "/logout" []
    :summary "remove user session"
    :return Result
    (auth/logout!))

  (GET "/products" []
    :summary "get list of products"
    ;:return Result
    ;(main/products!)
    (ok (db/get-products ))
    )

  (GET "/product/:id" []
    :summary "get product detail"
    :path-params [id :- Long]
    ;:return Result
    (ok (db/get-product {:id id} ))
    )

  (context "/api" []
    :tags ["thingie"]

    (GET "/plus" []
      :return       Long
      :query-params [x :- Long, {y :- Long 1}]
      :summary      "x+y with query-parameters. y defaults to 1."
      (ok (+ x y)))

    (POST "/minus" []
      :return      Long
      :body-params [x :- Long, y :- Long]
      :summary     "x-y with body-parameters."
      (ok (- x y)))

    (GET "/times/:x/:y" []
      :return      Long
      :path-params [x :- Long, y :- Long]
      :summary     "x*y with path-parameters"
      (ok (* x y)))

    (POST "/divide" []
      :return      Double
      :form-params [x :- Long, y :- Long]
      :summary     "x/y with form-parameters"
      (ok (/ x y)))

    (GET "/power" []
      :return      Long
      :header-params [x :- Long, y :- Long]
      :summary     "x^y with header-parameters"
      (ok (long (Math/pow x y))))))

(defapi restricted-service-routes
  {:swagger {:ui "/swagger-ui-private"
             :spec "/swagger-private.json"
             :data {:info {:version "1.0.0"
                           :title "private API"
                           :description "Private Services"}}}}

  (context "/admin" []

    (GET "/plus" []
      :return       Long
      :query-params [x :- Long, {y :- Long 1}]
      :summary      "x+y with query-parameters. y defaults to 1."
      (ok (+ x y)))

    (POST "/upload" req
      :multipart-params [file :- TempFileUpload , name :- String, price :- String, {description :- String ""}]
      :middleware [wrap-multipart-params]
      :summary "handles product upload"
      :return Result
      (upload/upload-product!
                              (:identity req)
                              file
                              name
                              price
                              description)
      )

    )
  )
