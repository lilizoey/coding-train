(ns coding-train.particle
  (:require [quil.core :as q])
  (:require [coding-train.ray :as ray]))

(defrecord Particle [pos rays])

(defn gen-ray [pos deg]
  (let [angle (q/radians deg)]
    (ray/look-at (ray/create pos) (map + pos [(q/sin angle) (q/cos angle)]))))

(defn create []
  (let [pos [(/ (q/width) 2) (/ (q/height) 2)]]
    (Particle. 
      pos
      (map (partial gen-ray pos) (range 0 360 2)))))
    
(defn draw-ellipse [[x y] [w h]]
  (q/ellipse x y w h))

(defn show [particle]
  (q/fill 255)
  (draw-ellipse (:pos particle) [4 4])
  (dorun (map ray/show (:rays particle))))

(defn vec-dist [[x1 y1] [x2 y2]]
  (q/dist x1 y1 x2 y2))

(defn closest-or-false [particle coll]
  (if (empty? coll)
    false
    (apply (partial min-key (partial vec-dist (:pos particle))) coll)))

(defn draw-raycast [particle cast]
  (when cast
    (q/line (:pos particle) cast)))

(defn look [particle walls]
  ; should've used reduce here loll 
  (let [res (map (fn [r] (->> walls
                              (map (partial ray/raycast r))
                              (filter identity)
                              (closest-or-false particle)))
                 (:rays particle))]
    (dorun (map (partial draw-raycast particle) res))))

(defn move-to 
  ([particle x y]
   (Particle. 
     [x y]
     (map #(ray/move % [x y]) (:rays particle))))
  ([particle [x y]] (move-to particle x y)))
