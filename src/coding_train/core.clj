(ns coding-train.core
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:require [coding-train.boundary :as b])
  (:require [coding-train.ray :as ray])
  (:require [coding-train.particle :as p]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  {:walls (map (fn [_] (b/create 
                         (* (rand) (q/width)) 
                         (* (rand) (q/width)) 
                         (* (rand) (q/height)) 
                         (* (rand) (q/height)))) 
            (range 5))
   :particle (p/create)
   :offset [0.0 10000.0]})

(defn update-state [state]
  (-> state
      (update :particle (fn [particle] (p/move-to particle (map * [(q/width) (q/height)] (map q/noise (:offset state))))))
      (update :offset #(into [] (map (partial + 0.01) %)))))


(defn draw-state [state]
  ; Clear the sketch by filling it with black color.
  (q/background 0) 
  (dorun (map b/show (:walls state)))
  (p/show (:particle state))
  (p/look (:particle state) (:walls state))
  state)  

(q/defsketch coding-train
  :title "2D Raycasting"
  :size [400 400]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])

(defn -main [& args])
