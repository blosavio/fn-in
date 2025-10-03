
  <body>
    <a href="https://clojars.org/com.sagevisuals/fn-in"><img src="https://img.shields.io/clojars/v/com.sagevisuals/fn-in.svg"></a><br>
    <a href="#setup">Setup</a><br>
    <a href="https://blosavio.github.io/fn-in/index.html">API</a><br>
    <a href="https://github.com/blosavio/fn-in/blob/master/changelog.md">Changelog</a><br>
    <a href="#introduction">Introduction</a><br>
    <a href="#ideas">Ideas</a><br>
    <a href="#performance">Performance</a><br>
    <a href="#examples">Examples</a><br>
    <a href="#alternatives">Alternatives</a><br>
    <a href="#glossary">Glossary</a><br>
    <a href="https://github.com/blosavio">Contact</a><br>
    <h1>
      fn-in
    </h1><em>A library for manipulating heterogeneous, arbitrarily-nested Clojure data structures</em><br>
    <section id="setup">
      <h2>
        Setup
      </h2>
      <h3>
        Leiningen/Boot
      </h3>
      <pre><code>[com.sagevisuals/fn-in &quot;5&quot;]</code></pre>
      <h3>
        Clojure CLI/deps.edn
      </h3>
      <pre><code>com.sagevisuals/fn-in {:mvn/version &quot;5&quot;}</code></pre>
      <h3>
        Require
      </h3>
      <pre><code>(require &apos;[fn-in.core :refer [get-in* assoc-in* update-in* dissoc-in*]])</code></pre>
    </section>
    <section id="introduction">
      <h2>
        Introduction
      </h2>
      <p>
        Most of the time, when someone hands us a Clojure collection, we know its &nbsp;type and how best to manipulate it. If we get handed a vector, we know
        that <code>get</code> is effective at retrieving an element. Let&apos;s grab that <code>3</code>.
      </p>
      <pre><code>(get [0 1 2 3 4 5] 2) ;; =&gt; 2</code></pre>
      <p>
        But sometimes, we won&apos;t know ahead of time what kind of collection &nbsp;someone might give us. Perhaps we made a commitment that our utility
        would &nbsp;handle <em>all</em> Clojure collection types. Instead of a vector, we might be given a lazy &nbsp;sequence, such as a <code>range</code>.
        Perhaps we want to pull out the third element.
      </p>
      <pre><code>(get (range 6) 2) ;; =&gt; nil</code></pre>
      <p>
        That&apos;s… not what we wanted. While <a href="https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get"><code>get</code></a>,
        <a href="https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/assoc"><code>assoc</code></a>, <a href=
        "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/update"><code>update</code></a>, and <a href=
        "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/dissoc"><code>dissoc</code></a> and their <code>...-in</code> cousins are
        exceedingly handy, they do not seamlessly handle every collection &nbsp;type.
      </p>
      <p>
        We could certainly build up an <em>ad hoc</em> type dispatch right on the spot inside our utility, but we&apos;ll probably &nbsp;forget to include at
        least one of the collection types, and we&apos;re likely to &nbsp;miss an edge case or two.
      </p>
      <p>
        This library endeavors to patch those gaps in functionality while &nbsp;maintaining a consistent, familiar interface to inspect, change, and remove
        <a href="#element">elements</a> contained in vectors, hashmaps, sequences, lists, and sets, at any arbitrary &nbsp;level of nesting.
      </p>
      <p>
        Leaning on those extended capabilities, we can now retrieve that tenth &nbsp;element with a straightforward, drop-in replacement.
      </p>
      <pre><code>(require &apos;[fn-in.core :refer [get*]])</code><br><br><code>(get* (range 6) 2) ;; =&gt; 2</code></pre>
      <p>
        <code>get*</code> behaves just like <code>clojure.core/get</code>, except that it succeeds in extracting the third element from our <code>range</code>
        argument.
      </p>
    </section>
    <section id="ideas">
      <h2>
        Ideas
      </h2>
      <p>
        This library provides <em>starred</em> versions of the core utilities — <code>get-in*</code>, <code>assoc-in*</code>, <code>update-in*</code>,
        <code>dissoc-in*</code> — which all operate similar to their <code>clojure.core</code> namesakes, but work on any <a href="#HANDS">heterogeneous,
        arbitrarily-nested data structure</a>.
      </p>
      <p>
        Their interface is based on the concept of a <a href="#path">path</a>. A path unambiguously addresses an element of a heterogeneous,
        &nbsp;arbitrarily-nested data structure. Elements in vectors, lists, and other &nbsp;sequences are addressed by zero-indexed integers. Hashmap elements
        are &nbsp;addressed by their keys, and set elements are addressed by the elements &nbsp;themselves.
      </p>
      <p>
        Here&apos;s how paths work. Vectors are addressed by zero-indexed integers.
      </p>
      <pre><code>           [100 101 102 103]</code><br><code>indexes --&gt; 0   1   2   3</code></pre>
      <p>
        Same for lists…
      </p>
      <pre><code>          &apos;(97 98 99 100)</code><br><code>indexes --&gt; 0  1  2  3</code></pre>
      <p>
        …and same for other sequences, like <code>range</code>.
      </p>
      <pre><code>(range 29 33) ;; =&gt; (29 30 31 32)</code><br><code>indexes -----------&gt; 0  1  2  3</code></pre>
      <p>
        Hashmaps are addressed by their keys, which are often keywords, like &nbsp;this.
      </p>
      <pre><code>        {:a 1 :foo &quot;bar&quot; :hello &apos;world}</code><br><code>keys --&gt; :a   :foo       :hello</code></pre>
      <p>
        But hashmaps may be keyed by <em>any</em> value, including integers…
      </p>
      <pre><code>        {0 &quot;zero&quot; 1 &quot;one&quot; 99 &quot;ninety-nine&quot;}</code><br><code>keys --&gt; 0        1       99</code></pre>
      <p>
        …or some other scalars…
      </p>
      <pre><code>        {&quot;a&quot; :value-at-str-key-a &apos;b :value-at-sym-key-b \c :value-at-char-key-c}</code><br><code>keys --&gt; &quot;a&quot;                     &apos;b                     \c</code></pre>
      <p>
        …even composite values.
      </p>
      <pre><code>        {[0] :val-at-vec-0 [1 2 3] :val-at-vec-1-2-3 {} :val-at-empty-map}</code><br><code>keys --&gt; [0]               [1 2 3]                   {}</code></pre>
      <p>
        Set elements are addressed by their identities, so they are located at &nbsp;themselves.
      </p>
      <pre><code>             #{42 :foo true 22/7}</code><br><code>identities --&gt; 42 :foo true 22/7</code></pre>
      <p>
        A <em>path</em> is a sequence of indexes, keys, or identities that &nbsp;allow the starred functions to dive into a nested data structure, one path
        &nbsp;element per level of nesting. Practically, any sequence may serve as a path, &nbsp;but a vector is convenient to make with a keyboard.
      </p>
      <p>
        Let&apos;s build a path to the third element of a vector <code>[11&nbsp;22&nbsp;33]</code>. Vector elements are addressed by zero-indexed integers, so
        the third &nbsp;element is located at integer <code>2</code>. We invoke <code>get-in*</code> just like <code>clojure.core/get-in</code>: the collection
        is the first arg. We stuff that <code>2</code> into the second arg, the path sequence.
      </p>
      <pre><code>(get-in* [11 22 33] [2]) ;; =&gt; 33</code></pre>
      <p>
        And we receive the third element, integer <code>33</code>.
      </p>
      <p>
        Let&apos;s get a little more fancy: a vector nested within another vector <code>[11&nbsp;22&nbsp;[33&nbsp;44&nbsp;55]]</code>. The nested vector is
        located at the third spot, index <code>2</code>. If we call <code>get-in*</code> with that path…
      </p>
      <pre><code>(get-in* [11 22 [33 44 55]] [2]) ;; =&gt; [33 44 55]</code></pre>
      <p>
        …it dutifully tells us that there&apos;s a child vector nested at that spot. To &nbsp;access the third element of <em>that</em> vector, we must append
        an entry onto the path.
      </p>
      <pre><code>(get-in* [11 22 [33 44 55]] [2 2]) ;; =&gt; 55</code></pre>
      <p>
        Nothing terribly special that <code>clojure.core/get-in</code> can&apos;t do. But, if for some reason, that nested thing is instead a list…
      </p>
      <pre><code>(get-in [11 22 &apos;(33 44 55)] [2 2]) ;; =&gt; nil</code></pre>
      <p>
        …it&apos;s not quite what we wanted. But if we invoke the starred version…
      </p>
      <pre><code>(get-in* [11 22 &apos;(33 44 55)] [2 2]) ;; =&gt; 55</code></pre>
      <p>
        …all fine and dandy.
      </p>
      <p>
        Let&apos;s look at hashmaps. Hashmap elements are addressed by keys. Let&apos;s &nbsp;inspect the value at key <code>:z</code>. We insert a
        <code>:z</code> keyword into the path arg.
      </p>
      <pre><code>(get-in* {:y 22, :z 33, :x 11} [:z]) ;; =&gt; 33</code></pre>
      <p>
        If there&apos;s another hashmap nested at that key, and we wanted the value at &nbsp;keyword <code>:w</code> of that nested hashmap, we would merely
        append that key to the previous path &nbsp;vector arg.
      </p>
      <pre><code>(get-in* {:y 22, :z {:q 44, :w 55}, :x 11} [:z :w]) ;; =&gt; 55</code></pre>
      <p>
        Again, that&apos;s exactly how <code>clojure.core/get-in</code> works, but what if we had something else nested there, like a
        <code>clojure.lang.Range</code>?
      </p>
      <pre><code>(get-in {:y 11, :z (range 30 33)} [:z 2]) ;; =&gt; nil</code></pre>
      <p>
        That <code>nil</code> may not be useful for what we need to do. But calling the starred version…
      </p>
      <pre><code>(get-in* {:y 11, :z (range 30 33)} [:z 2]) ;; =&gt; 32</code></pre>
      <p>
        …we get that <code>32</code> we wanted.
      </p>
      <p>
        Beyond inspecting a value with <code>get-in*</code>, the starred functions can return a modified copy of a heterogeneous, &nbsp;arbitrarily-nested data
        structure. They all consume a path exactly the way <code>get-in*</code> does. First, we could swap out — <em>associating</em> — a nested value for one
        we supply.
      </p>
      <p>
        Let&apos;s try associating an element contained in a <code>clojure.lang.Cycle</code> nested in a list, nested in a hashmap. Going three collections
        &apos;deep&apos; &nbsp;requires a three-element path.
      </p>
      <pre><code>(assoc-in* {:a (list 11 (take 3 (cycle [&apos;foo &apos;bar &apos;baz])))} [:a 1 2] :Clojure!)
;; =&gt; {:a (11 (foo bar :Clojure!))}</code></pre>
      <p>
        We could also apply a function to — <em>updating</em> — a nested value. Let&apos;s add <code>9977</code> to an integer contained in a vector, nested in
        a <code>clojure.lang.Repeat</code>. Diving two levels deep requires a two-element path.
      </p>
      <pre><code>(update-in* (take 3 (repeat [11 22 33])) [2 1] #(+ % 9977))
;; =&gt; ([11 22 33] [11 22 33] [11 9999 33])</code></pre>
      <p>
        Or, we can simply <em>dissociate</em> a nested value, removing it entirely. Let&apos;s dissociate an integer element &nbsp;contained in a
        <code>clojure.lang.Iterate</code>, nested in a list.
      </p>
      <pre><code>(dissoc-in* {:a (list 22 (take 3 (iterate inc 33)))} [:a 1 1])
;; =&gt; {:a (22 (33 35))}</code></pre>
      <p>
        Note how the starred functions are able to dive into any of the collection &nbsp;types to do their jobs. These capabilities allow us to
        straightforwardly &nbsp;manipulate any Clojure data we might encounter.
      </p>
      <p>
        (<code>clojure.core</code> does <a href="https://ask.clojure.org/index.php/730/missing-dissoc-in">not provide</a> an equivalent <code>dissoc-in</code>
        companion to <code>dissoc</code>.)
      </p>
      <p>
        The empty vector addresses the top-level root collection of any &nbsp;collection type.
      </p>
      <pre><code>(get-in* [1 2 3] []) ;; =&gt; [1 2 3]</code><br><br><code>(get-in* &apos;(:foo &quot;bar&quot; 42) []) ;; =&gt; (:foo &quot;bar&quot; 42)</code><br><br><code>(get-in* {:a 1, :b 2} []) ;; =&gt; {:a 1, :b 2}</code><br><br><code>(get-in* #{:foo 42 \z} []) ;; =&gt; #{42 :foo \z}</code><br><br><code>(get-in* (range 0 4) []) ;; =&gt; (0 1 2 3)</code></pre>
    </section>
    <section id="performance">
      <h2>
        Performance
      </h2>
      <p>
        The <code>fn-in</code> library endeavors to be a drop-in replacement for <code>get-in</code>, <code>assoc-in</code>, and friends, mimicking their
        signatures, semantics, etc. Where possible, <code>fn-in</code> delegates to those core functions, thus closely matching the <a href=
        "https://blosavio.github.io/fn-in/performance_summary.html">performance</a> expectations of the core functions, plus a smidgen of overhead for type
        &nbsp;dispatch.
      </p>
      <p>
        Some operations, such as manipulating the end of a list, will inevitably &nbsp;be slow. But even if not performant, at least the operation is
        <em>possible</em>.
      </p>
    </section>
    <section id="examples">
      <h2>
        Examples
      </h2>
      <h3>
        Getting values
      </h3>
      <pre><code>(get-in* [11 22 [33 44 55 [66 [77 [88 99]]]]] [2 3 1 1 1]) ;; =&gt; 99</code><br><br><code>(get-in* {:a {:b {:c {:d 99}}}} [:a :b :c :d]) ;; =&gt; 99</code><br><br><code>(get-in* (list 11 22 33 (list 44 (list 55))) [3 1 0]) ;; =&gt; 55</code><br><br><code>(get-in* #{11 #{22}} [#{22} 22]) ;; =&gt; 22</code><br><br><code>(get-in* [11 22 {:a 33, :b [44 55 66 {:c [77 88 99]}]}] [2 :b 3 :c 2]) ;; =&gt; 99</code><br><br><code>(get-in* {:a (list {} {:b [11 #{33}]})} [:a 1 :b 1 33]) ;; =&gt; 33</code></pre>
      <h3>
        Associating values
      </h3>
      <pre><code>(assoc-in* [11 [22 [33 [44 55 66]]]] [1 1 1 2] :new-val)
;; =&gt; [11 [22 [33 [44 55 :new-val]]]]</code><br><br><code>(assoc-in* {:a {:b {:c 42}}} [:a :b :c] 99) ;; =&gt; {:a {:b {:c 99}}}</code><br><br><code>(assoc-in* {:a [11 22 33 [44 55 {:b [66 {:c {:d 77}}]}]]}
&nbsp;          [:a 3 2 :b 1 :c :d]
&nbsp;          &quot;foo&quot;)
;; =&gt; {:a [11 22 33
;;         [44 55 {:b [66 {:c {:d &quot;foo&quot;}}]}]]}</code></pre>
      <h3>
        Updating values
      </h3>
      <pre><code>(update-in* [11 22 33 [44 [55 66 [77 88 99]]]] [3 1 2 2] inc)
;; =&gt; [11 22 33 [44 [55 66 [77 88 100]]]]</code><br><br><code>(update-in* {:a [11 22 {:b 33, :c [44 55 66 77]}]} [:a 2 :c 1] #(+ 5500 %))
;; =&gt; {:a [11 22 {:b 33, :c [44 5555 66 77]}]}</code></pre>
      <h3>
        Dissociating values
      </h3>
      <pre><code>(dissoc-in* [11 22 [33 [44 55 66]]] [2 1 1]) ;; =&gt; [11 22 [33 [44 66]]]</code><br><br><code>(dissoc-in* {:a [11 22 33 {:b 44, :c [55 66 77]}]} [:a 3 :c 0])
;; =&gt; {:a [11 22 33 {:b 44, :c [66 77]}]}</code></pre>
    </section>
    <section id="alternatives">
      <h2>
        Alternatives
      </h2>
      <ul>
        <li>
          <code>clojure.core</code>
          <p>
            Unless you absolutely need <code>fn-in</code>&apos;s capabilities, use the built-ins.
          </p>
        </li>
        <li>Plumatic&apos;s <a href="https://github.com/plumatic/plumbing">Plumbing</a>
          <p>
            A simple and <em>declarative</em> way to specify a structured computation, which is easy to analyze, change, compose, and monitor.
          </p>
        </li>
        <li>John Newman&apos;s <a href="https://github.com/johnmn3/injest">Injest</a>
          <p>
            Path thread macros for navigating into and transforming data.
          </p>
        </li>
        <li>Nathan Marz&apos; <a href="https://github.com/redplanetlabs/specter">Specter</a>
          <p>
            Querying and transforming nested and recursive data with a <em>navigator</em> abstraction.
          </p>
        </li>
      </ul>
    </section>
    <section id="glossary">
      <h2>
        Glossary
      </h2>
      <dl>
        <dt id="element">
          element
        </dt>
        <dd>
          <p>
            A thing contained within a collection, either a scalar value or another nested collection.
          </p>
        </dd>
        <dt id="HANDS">
          heterogeneous, arbitrarily-nested data structure
        </dt>
        <dd>
          <p>
            Exactly one Clojure collection (vector, map, list, sequence, or set) with zero or more <a href="#element">elements</a>, nested to any depth.
          </p>
        </dd>
        <dt id="non-term-seq">
          non-terminating sequence
        </dt>
        <dd>
          <p>
            One of <code>clojure.lang.{Cycle,Iterate,LazySeq,LongRange,Range,Repeat}</code> that may or may not be realized, and possibly infinite. (I am not
            aware of any way to determine if such a sequence is infinite, so I treat them as if they are.)
          </p>
        </dd>
        <dt id="path">
          path
        </dt>
        <dd>
          <p>
            A series of values that unambiguously navigates to a single <a href="#element">element</a> (scalar or sub-collection) in a <a href=
            "#HANDS">heterogeneous, arbitrarily-nested data structure</a>. In the context of the <code>fn-in</code> library, the series of values comprising a
            path is contained in a vector passed as the second argument to the namespace&apos;s <code>…-in</code> functions. Almost identical to the second
            argument of <a href="https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/get-in"><code>clojure.core/get-in</code></a>, but with
            more generality.
          </p>
          <p>
            Elements of vectors, lists, and other sequential collections are located by zero-indexed integers. Map values are addressed by their keys, which
            are often keywords, but can be any data type, including integers, or composite types. (You don&apos;t often need to key a map on a multi-element,
            nested structure, but when you need to, it&apos;s awesome.) Set members are addressed by their identities. Nested collections contained in a set
            can indeed be addressed: the path vector itself contains the collections. An empty vector <code>[]</code> addresses the outermost, containing
            collection.
          </p>
        </dd>
      </dl>
    </section><br>
    <h2>
      License
    </h2>
    <p></p>
    <p>
      This program and the accompanying materials are made available under the terms of the <a href="https://opensource.org/license/MIT">MIT License</a>.
    </p>
    <p></p>
    <p id="page-footer">
      Copyright © 2024–2025 Brad Losavio.<br>
      Compiled by <a href="https://github.com/blosavio/readmoi">ReadMoi</a> on 2025 October 03.<span id="uuid"><br>
      59ecaabc-1b75-4616-9f03-2ccde4bb8729</span>
    </p>
  </body>
</html>
