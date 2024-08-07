(ns api
  (:require
   [hiccup2.core :as h2]
   [hiccup.page :as page]
   [hiccup.element :as element]
   [hiccup.form :as form]
   [hiccup.util :as util]
   [fn-in-hiccup :refer :all]))


(def 123-UUID (random-uuid))


(spit "resources/html/_insert_html_filename_.html"
      (page-template
       "Project — >>>Insert Title<<<"
       123-UUID
       [:body
        (nav-bar "_insert_menu_entry_")
        [:article
         [:h1 "Project _insert_page_title_"]
         [:section
          [:p "Vulputate odio ut enim blandit volutpat maecenas volutpat blandit aliquam. Pharetra magna ac placerat vestibulum. Pharetra vel turpis nunc eget. Tincidunt lobortis feugiat vivamus at augue eget. Purus non enim praesent elementum. Mus mauris vitae ultricies leo integer malesuada. Pretium aenean pharetra magna ac placerat. Accumsan tortor posuere ac ut consequat semper viverra. Tincidunt id aliquet risus feugiat in ante metus dictum. Viverra tellus in hac habitasse platea dictumst vestibulum."]]]]))