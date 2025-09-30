
  <body>
    <h1>
      `update-in` performance
    </h1>
    <div>
      <a href="#group-0">Hashmaps</a><br>
      <a href="#group-1">Lists</a><br>
      <a href="#group-2">Sequences</a><br>
      <a href="#group-3">Vectors</a>
    </div>
    <div>
      <p>
        `update-in` preamble
      </p>
    </div>
    <section>
      <h3 id="group-0">
        Hashmaps
      </h3>
      <div>
        <p>
          Comments for hashmaps...
        </p>
      </div>
      <div>
        <h4 id="group-0-fexpr-0">
          (fn [n] (update-in (nested-map n) (path-map n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in (nested-map n) (path-map n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-0-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  2
                </th>
                <th>
                  3
                </th>
                <th>
                  4
                </th>
                <th>
                  5
                </th>
                <th>
                  6
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-22.edn">2.6e-07±3.6e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-23.edn">5.9e-07±1.2e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-24.edn">8.5e-07±2.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-25.edn">1.0e-06±1.1e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-26.edn">1.2e-06±2.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-27.edn">1.4e-06±3.0e-08</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-22.edn">2.2e-07±3.7e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-23.edn">4.6e-07±2.3e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-24.edn">6.5e-07±9.1e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-25.edn">7.9e-07±1.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-26.edn">9.1e-07±4.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-27.edn">1.1e-06±1.1e-08</a>
              </td>
            </tr>
          </table>
        </div>
        <h4 id="group-0-fexpr-1">
          (fn [n] (update-in* (nested-map n) (path-map n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in* (nested-map n) (path-map n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-0-fexpr-1.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  2
                </th>
                <th>
                  3
                </th>
                <th>
                  4
                </th>
                <th>
                  5
                </th>
                <th>
                  6
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-40.edn">3.9e-07±5.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-41.edn">1.1e-06±8.9e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-42.edn">1.6e-06±2.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-43.edn">2.2e-06±6.1e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-44.edn">2.7e-06±4.7e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-45.edn">3.2e-06±8.4e-08</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-40.edn">2.5e-07±1.6e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-41.edn">5.0e-07±2.0e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-42.edn">8.6e-07±1.1e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-43.edn">9.9e-07±9.7e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-44.edn">1.2e-06±5.3e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-45.edn">1.3e-06±7.3e-09</a>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <hr>
      <h3 id="group-1">
        Lists
      </h3>
      <div>
        <p>
          This is unfair to <code>get*</code>: <code>clojure.core/get</code> always returns <code>nil</code> when given a list, whereas <code>get*</code>
          actually retrieves the element.
        </p>
      </div>
      <div>
        <h4 id="group-1-fexpr-0">
          (fn [n] (update-in* (nested-list n) (path-list n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in* (nested-list n) (path-list n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-1-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="4">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  2
                </th>
                <th>
                  3
                </th>
                <th>
                  4
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-12.edn">1.3e-06±1.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-13.edn">4.3e-06±1.0e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-14.edn">1.1e-05±8.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-15.edn">2.0e-05±2.8e-07</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-12.edn">1.1e-06±8.4e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-13.edn">3.9e-06±2.7e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-14.edn">9.5e-06±9.9e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-15.edn">1.9e-05±2.4e-07</a>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <hr>
      <h3 id="group-2">
        Sequences
      </h3>
      <div>
        <p>
          Comments for sequences...
        </p>
      </div>
      <div>
        <h4 id="group-2-fexpr-0">
          (fn [n] (update-in* (nested-seq n) (path-seq n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in* (nested-seq n) (path-seq n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-2-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  2
                </th>
                <th>
                  3
                </th>
                <th>
                  4
                </th>
                <th>
                  5
                </th>
                <th>
                  6
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-28.edn">4.9e-07±9.1e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-29.edn">4.4e-06±6.6e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-30.edn">1.0e-05±1.7e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-31.edn">2.0e-05±2.3e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-32.edn">3.6e-05±5.7e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-33.edn">5.6e-05±6.4e-07</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-28.edn">3.1e-07±1.2e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-29.edn">4.0e-06±2.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-30.edn">9.6e-06±1.2e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-31.edn">1.9e-05±3.5e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-32.edn">3.3e-05±3.0e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-33.edn">5.3e-05±1.5e-07</a>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <hr>
      <h3 id="group-3">
        Vectors
      </h3>
      <div>
        <p>
          Comments for vectors...
        </p>
      </div>
      <div>
        <h4 id="group-3-fexpr-0">
          (fn [n] (update-in (narrow-deep-vec n) (path-narrow-deep-vec n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in (narrow-deep-vec n) (path-narrow-deep-vec n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-3-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  10
                </th>
                <th>
                  100
                </th>
                <th>
                  1000
                </th>
                <th>
                  10000
                </th>
                <th>
                  100000
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-6.edn">3.7e-07±3.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-7.edn">4.3e-07±3.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-8.edn">4.9e-07±8.4e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-9.edn">5.5e-07±7.6e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-10.edn">6.1e-07±4.9e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-11.edn">6.5e-07±6.9e-09</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-6.edn">4.0e-07±2.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-7.edn">4.3e-07±2.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-8.edn">4.6e-07±3.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-9.edn">4.9e-07±3.2e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-10.edn">5.3e-07±2.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-11.edn">5.5e-07±5.9e-09</a>
              </td>
            </tr>
          </table>
        </div>
        <h4 id="group-3-fexpr-1">
          (fn [n] (update-in (nested-vec n) (path-nested-vec n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in (nested-vec n) (path-nested-vec n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-3-fexpr-1.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  2
                </th>
                <th>
                  3
                </th>
                <th>
                  4
                </th>
                <th>
                  5
                </th>
                <th>
                  6
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-16.edn">2.4e-07±5.0e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-17.edn">3.3e-07±3.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-18.edn">4.6e-07±1.3e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-19.edn">5.4e-07±1.1e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-20.edn">6.7e-07±8.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-21.edn">7.6e-07±1.2e-08</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-16.edn">2.1e-07±9.6e-10</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-17.edn">2.8e-07±2.1e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-18.edn">3.8e-07±1.0e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-19.edn">4.5e-07±1.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-20.edn">5.5e-07±3.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-21.edn">6.3e-07±6.9e-09</a>
              </td>
            </tr>
          </table>
        </div>
        <h4 id="group-3-fexpr-2">
          (fn [n] (update-in* (narrow-deep-vec n) (path-narrow-deep-vec n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in* (narrow-deep-vec n) (path-narrow-deep-vec n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-3-fexpr-2.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  10
                </th>
                <th>
                  100
                </th>
                <th>
                  1000
                </th>
                <th>
                  10000
                </th>
                <th>
                  100000
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-34.edn">1.9e-06±1.9e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-35.edn">2.0e-06±1.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-36.edn">2.0e-06±5.3e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-37.edn">2.0e-06±1.1e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-38.edn">2.1e-06±1.9e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-39.edn">2.1e-06±2.7e-08</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-34.edn">8.1e-07±8.9e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-35.edn">8.3e-07±7.6e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-36.edn">8.6e-07±2.1e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-37.edn">9.0e-07±1.3e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-38.edn">9.3e-07±6.1e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-39.edn">9.5e-07±4.7e-09</a>
              </td>
            </tr>
          </table>
        </div>
        <h4 id="group-3-fexpr-3">
          (fn [n] (update-in* (nested-vec n) (path-nested-vec n) inc))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (update-in* (nested-vec n) (path-nested-vec n) inc))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_update_in/group-3-fexpr-3.svg"><button class="collapser" type="button">Show details</button>
        <div class="collapsable">
          <table>
            <caption>
              times in seconds, <em>mean±std</em>
            </caption>
            <thead>
              <tr>
                <td></td>
                <th colspan="6">
                  arg, n
                </th>
              </tr>
              <tr>
                <th>
                  version
                </th>
                <th>
                  1
                </th>
                <th>
                  2
                </th>
                <th>
                  3
                </th>
                <th>
                  4
                </th>
                <th>
                  5
                </th>
                <th>
                  6
                </th>
              </tr>
            </thead>
            <tr>
              <td>
                4
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-0.edn">4.5e-07±2.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-1.edn">8.7e-07±9.4e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-2.edn">1.3e-06±2.4e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-3.edn">1.7e-06±2.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-4.edn">2.1e-06±2.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 4/test-5.edn">2.5e-06±4.8e-08</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-0.edn">2.3e-07±1.1e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-1.edn">4.1e-07±5.7e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-2.edn">5.8e-07±2.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-3.edn">7.4e-07±5.6e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-4.edn">9.0e-07±8.7e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn-in/blob/master/resources/fn_in_performance/update_in/version 5/test-5.edn">1.1e-06±2.2e-08</a>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <hr>
    </section>
    <p id="page-footer">
      Copyright © 2024–2025 Brad Losavio.<br>
      Compiled by <a href="https://github.com/blosavio/Fastester">Fastester</a> on 2025 September 30.<span id="uuid"><br>
      ffaad48e-c77b-48dc-bd5f-8c4af5332cf5</span>
    </p>
  </body>
</html>
