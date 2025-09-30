
  <body>
    <h1>
      `dissoc-in` performance
    </h1>
    <div>
      <a href="#group-0">Hashmaps</a><br>
      <a href="#group-1">Lists</a><br>
      <a href="#group-2">Sequences</a><br>
      <a href="#group-3">Vectors</a>
    </div>
    <div>
      <p>
        `dissoc-in` preamble
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
          (fn [n] (dissoc-in* (nested-map n) (path-map n)))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (dissoc-in* (nested-map n) (path-map n)))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_dissoc_in/group-0-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
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
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-16.edn">2.4e-07±1.8e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-17.edn">6.3e-07±8.9e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-18.edn">1.2e-06±8.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-19.edn">1.7e-06±3.7e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-20.edn">2.2e-06±2.9e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-21.edn">2.6e-06±6.1e-09</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-16.edn">9.3e-08±6.7e-10</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-17.edn">3.3e-07±7.9e-10</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-18.edn">8.0e-07±6.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-19.edn">9.4e-07±2.6e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-20.edn">1.1e-06±5.3e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-21.edn">1.2e-06±7.0e-09</a>
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
          (fn [n] (dissoc-in* (nested-list n) (path-list n)))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (dissoc-in* (nested-list n) (path-list n)))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_dissoc_in/group-1-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
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
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-12.edn">5.3e-07±4.2e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-13.edn">3.7e-06±2.2e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-14.edn">9.9e-06±4.4e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-15.edn">2.0e-05±1.2e-07</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-12.edn">4.4e-07±1.8e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-13.edn">3.2e-06±1.6e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-14.edn">8.8e-06±1.0e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-15.edn">1.8e-05±6.4e-08</a>
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
          (fn [n] (dissoc-in* (nested-seq n) (path-seq n)))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (dissoc-in* (nested-seq n) (path-seq n)))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_dissoc_in/group-2-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
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
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-22.edn">1.7e-07±2.7e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-23.edn">3.8e-06±4.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-24.edn">1.0e-05±8.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-25.edn">2.0e-05±2.1e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-26.edn">3.6e-05±5.5e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-27.edn">5.6e-05±1.8e-07</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-22.edn">1.2e-07±2.3e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-23.edn">3.3e-06±1.3e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-24.edn">8.9e-06±7.4e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-25.edn">1.8e-05±6.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-26.edn">3.4e-05±9.5e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-27.edn">5.4e-05±8.7e-07</a>
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
          (fn [n] (dissoc-in* (narrow-deep-vec n) (path-narrow-deep-vec n)))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (dissoc-in* (narrow-deep-vec n) (path-narrow-deep-vec n)))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_dissoc_in/group-3-fexpr-0.svg"><button class="collapser" type="button">Show details</button>
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
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-0.edn">1.8e-06±2.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-1.edn">3.1e-06±1.2e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-2.edn">1.7e-05±9.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-3.edn">1.5e-04±1.4e-06</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-4.edn">1.5e-03±1.1e-05</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-5.edn">1.6e-02±1.6e-04</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-0.edn">1.0e-06±1.8e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-1.edn">2.2e-06±3.5e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-2.edn">1.6e-05±4.3e-07</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-3.edn">1.5e-04±1.5e-06</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-4.edn">1.5e-03±9.4e-06</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-5.edn">1.5e-02±2.4e-04</a>
              </td>
            </tr>
          </table>
        </div>
        <h4 id="group-3-fexpr-1">
          (fn [n] (dissoc-in* (nested-vec n) (path-nested-vec n)))
        </h4><img alt=
        "Benchmark measurements for expression `(fn [n] (dissoc-in* (nested-vec n) (path-nested-vec n)))`, time versus &apos;n&apos; arguments, comparing different versions."
        src="img_dissoc_in/group-3-fexpr-1.svg"><button class="collapser" type="button">Show details</button>
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
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-6.edn">6.0e-07±3.5e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-7.edn">1.2e-06±7.4e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-8.edn">1.8e-06±8.3e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-9.edn">2.4e-06±1.9e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-10.edn">3.1e-06±4.0e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 4/test-11.edn">3.6e-06±3.1e-08</a>
              </td>
            </tr>
            <tr>
              <td>
                5
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-6.edn">4.6e-07±2.9e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-7.edn">8.6e-07±6.0e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-8.edn">1.2e-06±2.2e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-9.edn">1.5e-06±2.2e-08</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-10.edn">1.8e-06±9.2e-09</a>
              </td>
              <td>
                <a href="https://github.com/blosavio/fn_in/blob/main/resources/fn_in_performance/dissoc_in/version 5/test-11.edn">2.2e-06±1.0e-08</a>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <hr>
    </section>
    <p id="page-footer">
      Copyright © 2024–2025 Brad Losavio.<br>
      Compiled by <a href="https://github.com/blosavio/Fastester">Fastester</a> on 2025 September 29.<span id="uuid"><br>
      abd643c6-c697-4d28-b491-837e69bfa3e0</span>
    </p>
  </body>
</html>
