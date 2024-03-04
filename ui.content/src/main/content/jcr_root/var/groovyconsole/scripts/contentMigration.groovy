import javax.jcr.Node
import org.apache.sling.api.resource.path.PathBuilder
import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.Replicator

pagesForReplication = []

def findParentPage(node) {
    if (node && node.depth != 0) {
        def parent = node.parent
        if (parent) {
            def primaryType = node.getProperties("jcr:primaryType").next().value.toString()
            if (primaryType == "cq:Page") {
                println("adding this page" + node.getPath())
                pagesForReplication << node.getPath()
            } else {
                findParentPage(parent)
            }
        }
    }
}

def wrapInnerNodes(Node node) {
    if (node.name == "inner-wrapper") {
        return
    }

    def innerNodes = node.nodes.findAll { it.name == "inner" }
    innerNodes.each { innerNode ->
        def parentNode = innerNode.parent

        def newParentNode = parentNode.addNode("inner-wrapper", "nt:unstructured")
        newParentNode.setProperty("sling:resourceType", "my-project/components/wrapper")

        String targetPath = new PathBuilder(newParentNode.getPath()).append(innerNode.getName()).toString()
        findParentPage(innerNode)
        session.move(innerNode.getPath(), targetPath)
        session.save()
    }
}

def traverseAndWrapNodes(Node node) {
    wrapInnerNodes(node)
    if (node.hasNodes()) {
        def nodeIterator = node.nodes
        while (nodeIterator.hasNext()) {
            traverseAndWrapNodes(nodeIterator.nextNode())
        }
    }
}

def main() {
    def root = session.getNode("/content/mysite/de_DE")
    traverseAndWrapNodes(root)
    session.save()
}

main()
println(pagesForReplication)

//PUBLISH PAGES
Replicator replicator = getService(Replicator.class)
for(final String path : pagesForReplication){
    replicator.replicate(session, ReplicationActionType.ACTIVATE, path)
}
