(ns coding-train.boundary
  (:require [quil.core :as q]))

(defrecord Boundary [a b])

(defn create [x1 y1 x2 y2]
  "Create a boundary with a and b as vectors."
  (Boundary. [x1 y1] [x2 y2]))

(defn show [boundary]
  "Draw the given boundary to the screen."
  (q/stroke 255)
  (q/line (:a boundary) (:b boundary)))
