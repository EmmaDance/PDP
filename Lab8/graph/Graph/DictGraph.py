'''
Created on Feb 28, 2018

@author: ema
'''


class DictGraph:
    '''
    A directed graph, represented as three maps,
    one from each vertex to the set of outbound neighbours,
    one from each vertex to the set of inbound neighbours, and one with the costs of each edge.
    '''

    def __init__(self, n):
        '''
        Creates a graph with n vertices (0 - n-1) and no edges.
        '''
        self.__in = {}
        self.__out = {}
        self.__cost = {}
        self.__vertices = []
        for i in range(n):
            self.__vertices.append(i)
        for i in range(n):
            self.__in[i] = []
            self.__out[i] = []

    def addVertex(self, v):
        '''
        Adds a new isolated vertex.
        Precondition: there is no vertex v
        '''
        if v in self.__in or v in self.__out:
            return False
        self.__in[v] = []
        self.__out[v] = []
        self.__vertices.append(v)
        return True

    def removeVertex(self, v):
        '''
        Removes a vertex from the graph
        Input: v - the vertex to be removed
        Precondition: v must be a vertex of the graph
        '''
        for k in self.__out.keys():
            if v in self.__in[k]:
                self.removeEdge(v, k)
        self.__in.pop(v)
        self.__out.pop(v)
        self.__vertices.remove(v)

    def addEdge(self, x, y, c):
        '''
        Adds an edge of cost c from x to y.
        Precondition: there is no edge from x to y
        '''
        if self.isEdge(x, y):
            return False
        self.__out[x].append(y)
        self.__in[y].append(x)
        self.__cost[(x, y)] = c
        return True

    def removeEdge(self, x, y):
        ''' 
        Removes the edge (x,y) from the graph
        Input: x,y - endpoints of the edge to be removed
        Precondition: (x,y) is an edge of the graph
        '''
        if not self.isEdge(x, y):
            return False
        self.__out[x].remove(y)
        self.__in[y].remove(x)
        del self.__cost[(x, y)]
        return True

    def isEdge(self, x, y):
        """
        Returns - True if there is an edge from x to y
                - False otherwise
        """
        return y in self.__out[x] or x in self.__in[y]

    def getInDegree(self, v):
        return len(self.__in[v])

    def getOutDegree(self, v):
        return len(self.__out[v])

    def parseOut(self, v):
        """
        Returns an iterable containing the outbound neighbours of v
        """
        return self.__out[v]

    def parseIn(self, v):
        """
        Returns an iterable containing the inbound neighbours of v
        """
        return self.__in[v]

    def getCost(self, x, y):
        '''
        Returns the cost of the edge (x,y)
        '''
        return self.__cost[(x, y)]

    def setCost(self, x, y, c):
        '''
        Modifies the cost of edge (x, y) to be c
        '''
        self.__cost[(x, y)] = c

    def getNumberOfVertices(self):
        '''
        Returns the number of vertices in the graph
        '''
        return len(self.__in.keys())

    def getVertices(self):
        '''
        Returns the vertices in the graph
        '''
        return self.__vertices

    def validVertex(self, v):
        return v in self.__vertices

    def is_hamiltonian(self, arr):
        '''
           arr - an array of vertices
        '''
        if not len(arr) == self.getNumberOfVertices():
            return False
        s = set(arr)
        if len(s) != len(arr):
            return False
        for i in range(0, len(arr)-1):
            if not self.isEdge(arr[i], arr[i+1]):
                return False
        if not self.isEdge(arr[-1], arr[0]):
            return False
        return True


def testGraph():
    g = DictGraph(4)
    g.addEdge(1, 2, 1)
    g.addEdge(1, 3, 2)
    g.addEdge(2, 3, 1)
    g.addEdge(0, 2, 1)
    g.addEdge(0, 1, 5)
    assert g.isEdge(1, 2) == True


testGraph()
