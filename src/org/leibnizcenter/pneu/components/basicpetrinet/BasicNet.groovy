package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType
import org.leibnizcenter.pneu.parsers.PNML2PN

class BasicNet extends Net {

    // an emitter transition is a natural input
    Transition createEmitterTransition() {
        BasicTransition tr = new BasicTransition(type: TransitionType.EMITTER)
        transitionList << tr
        inputs << tr
        tr
    }

    // a collector transition is a natural output
    Transition createCollectorTransition() {
        BasicTransition tr = new BasicTransition(type: TransitionType.COLLECTOR)
        transitionList << tr
        outputs << tr
        tr
    }

    Transition createTransition(String label = null) {
        BasicTransition tr = new BasicTransition(name: label)
        transitionList << tr
        tr
    }

    Place createPlace(String label = null) {
        BasicPlace pl = new BasicPlace(name: label)
        placeList << pl
        pl
    }

    // deep cloning done for nets
    // only the net structure is cloned, all the elements remains the same (e.g. places, transitions, etc.)
    Net minimalClone(Map<Net, Net> sourceCloneMap = [:]) {

        if (!sourceCloneMap[this]) {
            sourceCloneMap[this] = new BasicNet(transitionList: transitionList.collect(),
                    placeList: placeList.collect(),
                    arcList: arcList.collect(),
                    inputs: inputs.collect(),
                    outputs: outputs.collect(),
                    function: function)
        }

        Net clone = sourceCloneMap[this]

        for (subNet in subNets) {
            if (!sourceCloneMap[subNet]) {
                sourceCloneMap[subNet] = subNet.minimalClone(sourceCloneMap)
            }
            clone.subNets << sourceCloneMap[subNet]
        }

        for (parent in parents) {
            if (!sourceCloneMap[parent]) {
                sourceCloneMap[parent] = parent.minimalClone(sourceCloneMap)
            }
            clone.parents << sourceCloneMap[parent]
        }

        clone
    }

    //////////////////////////////////////////////////////
    // helpers for importing the net
    //////////////////////////////////////////////////////

    static Net importFromPNML(String filename) {

        Net net

        print("reading from file " + filename + "... ");
        try {
            net = PNML2PN.parseFile(filename)
        } catch (FileNotFoundException) {
            throw new RuntimeException("sorry, file " + filename + " not found or not valid.")
        }

        net
    }

}