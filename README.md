# pneu 

**Petri Net pnEUmatics**
*groovy libraries/scripts to tinker with Petri Nets* 

(still quite a prototype)

## Components

* basic Petri nets model (not distinguishable tokens)
* brute-force simulator 
* exporter to json (with current marking)
* analyzer (depth-first search of execution path)
* single-entry-single-exit (SESE) decomponser of traces
* importer from Yasper PNML 
* exporter to tikz (LaTeX!) 
* exporter to dot (graphviz, etc.) 

Yasper is a visual editor for basic petri nets. http://yasper.org. PNML is the Petri Net Markup Language, a XML-based format. 

## Usage examples

**basic Petri net creation**
```
Net net = new BasicNet()
Transition tIn = net.createEmitterTransition()
Place pA = net.createPlace("a")
Place pB = net.createPlace("b")
Place pC = net.createPlace("c")
Place pD = net.createPlace("d")
Place pE = net.createPlace("e")

net.createArc(tIn, pA)
net.createBridge(pA, pB)
net.createBridge(pB, pC)
net.createBridge(pA, pD)
net.createBridge(pD, pE)

Transition tOut = net.createCollectorTransition()
net.createArc(pC, tOut)
net.createArc(pE, tOut)

net.resetIds() 
```

**monolithic simulation**
```
NetRunner runner = new NetRunner()
runner.load(net)
runner.run(2) // to make 2 discrete steps
```

**json export**
```
net.exportToJson("readmeNet")
```

output: /out/json/readmeNet.json
```
{
  "places": [
    {"id": "p0", "label": "a"},
    {"id": "p1", "label": "b"},
    {"id": "p2", "label": "c", "marking": [""]},
    {"id": "p3", "label": "d"},
    {"id": "p4", "label": "e"}
  ],
  "transitions": [
    {"id": "t0"},
    {"id": "t1"},
    {"id": "t2"},
    {"id": "t3"},
    {"id": "t4"},
    {"id": "t5"}
  ],
  "arcs": [
    { "source": "t3", "target": "p0" },
    { "source": "p0", "target": "t4" },
    { "source": "t4", "target": "p1" },
    { "source": "p1", "target": "t5" },
    { "source": "t5", "target": "p2" },
    { "source": "p0", "target": "t0" },
    { "source": "t0", "target": "p3" },
    { "source": "p3", "target": "t1" },
    { "source": "t1", "target": "p4" },
    { "source": "p2", "target": "t2" },
    { "source": "p4", "target": "t2" }
  ]
} 
```

**analysis**
```
NetRunner runner = new NetRunner()
runner.load(net)
runner.analyse()
runner.analysis.exportToLog("readmeNet")
```

output: /out/log/analysis/readmeNet.analysis.log
```
Summary: 
0: t0.t0, t1.t1, t2.t2.
1: t0.t0, t3.t3, t4.t4.

Stories: 
(st0) -- [t0] -- (st1) -- [t1] -- (st2) -- [t2] -- (st3)
(st0) -- [t0] -- (st1) -- [t3] -- (st4) -- [t4] -- (st5)

States: 
st0: [p0 (0), p1 (0), p2 (0), p3 (0), p4 (0)] / [t0 => (st1)] 
st1: [p0 (1), p1 (0), p2 (0), p3 (0), p4 (0)] / [t1 => (st2), t3 => (st4)] 
st2: [p0 (0), p1 (1), p2 (0), p3 (0), p4 (0)] / [t2 => (st3)] 
st3: [p0 (0), p1 (0), p2 (1), p3 (0), p4 (0)] / [] 
st4: [p0 (0), p1 (0), p2 (0), p3 (1), p4 (0)] / [t4 => (st5)] 
st5: [p0 (0), p1 (0), p2 (0), p3 (0), p4 (1)] / [] 
```

**dot export**
```
net.exportToDot("readmeNet")
```

**PNML parsing:**
```
net = BasicNet.importFromPNML("examples/basic/7reset.pnml")
```

**tikz conversion (for LaTeX):**
```
net.exportToLaTeX("7reset")
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request