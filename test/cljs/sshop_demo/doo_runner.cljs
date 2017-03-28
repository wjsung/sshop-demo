(ns sshop-demo.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [sshop-demo.core-test]))

(doo-tests 'sshop-demo.core-test)

