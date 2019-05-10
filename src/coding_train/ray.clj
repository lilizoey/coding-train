(ns coding-train.ray
  (:require [quil.core :as q]))

(defrecord Ray [pos dir])

(defn create [pos]
  "Create a Ray with the given position."
  (Ray. pos [1 0]))

(defn show [ray]
  "Draw the given Ray to the screen"
  (q/stroke 255)
  (q/line (:pos ray) (map #(+ %1 (* 10 %2)) (:pos ray) (:dir ray))))
  
(defn normalize [vec]
  "Normalize a vector with 2 values."
  (let [magnitude (Math/sqrt (apply + (map * vec vec)))]
    (map #(/ % magnitude) vec)))

(defn look-at [ray vec]
  (Ray. 
    (:pos ray)
    (normalize (map - vec (:pos ray)))))
    
(defn move [ray pos]
  (Ray. pos (:dir ray)))

(defmacro with-rw [ray wall body]
  (list 'let 
    [['x1 'y1] (list :a wall)
     ['x2 'y2] (list :b wall)
     ['x3 'y3] (list :pos ray)
     ['x4 'y4] (list map + (list :pos ray) (list :dir ray))] body))

(defn calc-denominator [ray wall]
  (with-rw ray wall 
    (-
       (* (- x1 x2) (- y3 y4))
       (* (- y1 y2) (- x3 x4)))))

(defn calc-t [ray wall denom]
  (if (zero? denom)
    false
    (with-rw ray wall
       (/
          (-
             (* (- x1 x3) (- y3 y4))
             (* (- y1 y3) (- x3 x4)))
          denom))))

(defn calc-u [ray wall denom]
  (if (zero? denom)
    false
    (with-rw ray wall
      (- (/
          (-
             (* (- x1 x2) (- y1 y3))
             (* (- y1 y2) (- x1 x3)))
          denom)))))

(defn raycast [ray wall]
  (let 
    [denom (calc-denominator ray wall)
     t (calc-t ray wall denom)
     u (calc-u ray wall denom)]
    (if (and
             (not (zero? denom))
             (> t 0)
             (< t 1)
             (> u 0))
      (with-rw ray wall
        [(+ x1 (* t (- x2 x1)))
         (+ y1 (* t (- y2 y1)))])
      false)))
         
      
  
