(ns redframe.events
  (:require
   [re-frame.core :as rf]))

(defn- get-params-without-id [params]
  (when-let [event-params (first params)]
    (vec (rest event-params))))

(defn- get-event-db-fn [ns-key f]
  (fn [db & params]
    (let [ns-db (get db ns-key)
          event-params (get-params-without-id params)
          result (apply f (conj [ns-db] event-params))]
      (update db ns-key #(merge % result)))))

(defn reg-db-events [ns-key db-events]
  (doseq [[k v] db-events]
    (if (map? v)
      (let [i (:interceptors v)
            f (get-event-db-fn ns-key (:handler v))]
        (apply rf/reg-event-db [k i f]))
      (let [f (get-event-db-fn ns-key v)]
        (apply rf/reg-event-db [k f])))))

(rf/reg-event-db
 ::set-ns-db
 (fn [db [_ ns-key data]]
   (update db ns-key #(merge % data))))

(defn- set-fx-fns [ns-key fx]
  (let [db (:db fx)]
    (merge
     fx
     {:ns (get (:db fx) ns-key)
      :set-ns (fn [data]
                (rf/dispatch [::set-ns-db ns-key data]))
      :set-db (fn [data] (merge db data))
      :call (fn [k & rst]
              (rf/dispatch (into [k] rst)))})))

(defn- get-event-fx-fn [ns-key f]
  (fn [fx & params]
    (let [event-params (get-params-without-id params)]
      (apply f (conj [(set-fx-fns ns-key fx)] event-params))
      nil)))

(defn reg-fx-events [ns-key fx-events]
  (doseq [[k v] fx-events]
    (if (map? v)
      (let [i (:interceptors v)
            f (get-event-fx-fn ns-key (:handler v))]
        (apply rf/reg-event-fx [k i f]))
      (let [f (get-event-fx-fn ns-key v)]
        (apply rf/reg-event-fx [k f])))))
