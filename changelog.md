
  <body>
    <h1>
      fn-in library changelog
    </h1><a href="https://github.com/blosavio/chlog">changelog info</a>
    <section>
      <h3 id="v6">
        version 6
      </h3>
      <p>
        2025 November 7<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> TODO: Bugfix: Extended handling of `nil` to properly replicate behavior of core utilities. TODO: Added handling for Java arrays.
        TODO: Added handling for queues. TODO: Added handling for `clojure.core.Vec` (created by `vector-of`). Improved unit tests; added property tests.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> medium<br>
        <em>Breaking:</em> no
      </p>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul></ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Reorganized and streamlined unit tests and added inline checks to verify the type of the collections being tested.
            </div>
          </li>
          <li>
            <div>
              Added property tests.
            </div>
          </li>
        </ul>
      </div>
      <hr>
    </section>
    <section>
      <h3 id="v5">
        version 5
      </h3>
      <p>
        2025 September 27<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> Performance enhancements and additional arities.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> medium<br>
        <em>Breaking:</em> no
      </p>
      <p></p>
      <div>
        <em>altered functions:</em> <code>assoc*</code>, <code>assoc-in*</code>, <code>dissoc*</code>, <code>dissoc-in*</code>, <code>get*</code>,
        <code>get-in*</code>, <code>update*</code>, <code>update-in*</code>
      </div>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul></ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul>
          <li>
            <div>
              To improve performance, changed type dispatch from multimethods to protocols. Performance improved for many cases, particularly when handling
              hashmaps and vectors. In some cases, functions nearly match their clojure.core namesakes. Observed no significant performance regressions.
            </div>
          </li>
          <li>
            <div>
              `get*` and `get-in*` now support &apos;not-found&apos; arity.
            </div>
          </li>
          <li>
            <div>
              `assoc-in*` now correctly throws an exception when given an empty path.
            </div>
          </li>
          <li>
            <div>
              All core functions upgraded to handle records.
            </div>
          </li>
        </ul>
      </div>
      <hr>
    </section>
    <section>
      <h3 id="v4">
        version 4
      </h3>
      <p>
        2024 November 27<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> Moved changelog-generating scripts to external lib. Requires new dev (non-breaking) dependency.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> medium<br>
        <em>Breaking:</em> yes
      </p>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Removed `fn-in-project-changelog-generator` namespace.
            </div>
          </li>
        </ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Delegate changelog generation to Chlog lib, a dev-time dependency.
            </div>
          </li>
          <li>
            <div>
              Changed `test.check` from a full dependency to a development dependency.
            </div>
          </li>
        </ul>
      </div>
      <hr>
    </section>
    <section>
      <h3 id="v3">
        version 3
      </h3>
      <p>
        2024 November 19<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> Moved ReadMe-generating scripts to external lib. Requires new dev (non-breaking) dependency.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> low<br>
        <em>Breaking:</em> yes
      </p>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Require Clojure v 1.12.0
            </div>
          </li>
        </ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Moved ReadMe generation into `ReadMoi` lib.
            </div>
          </li>
        </ul>
      </div>
      <hr>
    </section>
    <section>
      <h3 id="v2">
        version 2
      </h3>
      <p>
        2024 November 9<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> Expanded collection hierarchy so that &apos;SubVectors&apos; of type `clojure.lang.APersistentVector$SubVector` are handled by
        all core functions.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> low<br>
        <em>Breaking:</em> no
      </p>
      <p></p>
      <div>
        <em>altered functions:</em> <code>assoc*</code>, <code>assoc-in*</code>, <code>dissoc*</code>, <code>dissoc-in*</code>, <code>get*</code>,
        <code>get-in*</code>, <code>update*</code>, <code>update-in*</code>
      </div>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul></ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Added ability for all core functions to handle subvectors.
            </div>
          </li>
          <li>
            <div>
              Added changelog.md generation to better communicate changes.
            </div>
          </li>
        </ul>
      </div>
      <hr>
    </section>
    <section>
      <h3 id="v1">
        version 1
      </h3>
      <p>
        2024 August 7<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> Streamlined docs, with more accurate links between Clojars and Github.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> low<br>
        <em>Breaking:</em> no
      </p>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul></ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul>
          <li>
            <div>
              Added an explicit `cljdoc.doc/tree` to try to keep clj-doc&apos;s auto-generation from choking.
            </div>
          </li>
        </ul>
      </div>
      <hr>
    </section>
    <section>
      <h3 id="v0">
        version 0
      </h3>
      <p>
        2024 August 7<br>
        Brad Losavio (blosavio@sagevisuals.com)<br>
        <em>Description:</em> Initial release.<br>
        <em>Project status:</em> <a href="https://github.com/metosin/open-source/blob/main/project-status.md">active</a><br>
        <em>Urgency:</em> low<br>
        <em>Breaking:</em> no
      </p>
      <p></p>
      <div>
        <h4>
          Breaking changes
        </h4>
        <ul></ul>
        <h4>
          Non-breaking changes
        </h4>
        <ul></ul>
      </div>
      <hr>
    </section>
    <p id="page-footer">
      Copyright © 2024–2025 Brad Losavio.<br>
      Compiled by <a href="https://github.com/blosavio/chlog">Chlog</a> on 2025 November 15.<span id="uuid"><br>
      59ecaabc-1b75-4616-9f03-2ccde4bb8729</span>
    </p>
  </body>
</html>
