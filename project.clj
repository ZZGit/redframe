(defproject redframe "0.1.0-SNAPSHOT"
  :description "redframe"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :sign-releases false}]]
  :dependencies [[re-frame "1.1.1"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.610"]])
