package pneu

import pneu.animation.monolithic.NetRunner
import pneu.components.basicpetrinet.BasicNet
import pneu.components.petrinet.Net
import pneu.components.petrinet.Place
import pneu.components.petrinet.Transition

class ReadmeTest extends GroovyTestCase {

    void testReadme() {

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

        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.run(3)

        net.exportToDot("readmeNet")
        net.exportToJson("readmeNet")

        net = BasicNet.importFromPNML("examples/basic/7reset.pnml")
        net.exportToLaTeX("7reset")
    }

}
