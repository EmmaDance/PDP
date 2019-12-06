import copy
import logging
import threading



class Algorithm:
    def __init__(self, graph, nrThreads):
        self.graph = graph
        self.found = False
        self.solution = []
        self.nrThreads = nrThreads

    def startParallel(self):
        arr = []
        for i in range(0, self.graph.getNumberOfVertices()):
            arr.append(-1)
        self.searchParallel(arr, 0, self.nrThreads)

    def start(self):
        arr = []
        for i in range(0, self.graph.getNumberOfVertices()):
            arr.append(-1)
        self.search(arr, 0)

    def searchParallel(self, arr, pos, n_threads):
        if self.found:
            return
        if pos >= len(arr):
            if self.graph.is_hamiltonian(arr):
                if not self.found:
                    self.found = True
                    self.solution = arr
                    print(arr)
            return
        vertices = self.graph.getVertices()
        nv = self.graph.getNumberOfVertices()
        if n_threads > 1:
            for i in range(0, nv):
                if vertices[i] not in arr:
                    arr1 = copy.deepcopy(arr)
                    arr1[pos] = vertices[i]
                    t = threading.Thread(target=self.searchParallel, args=(arr1, pos + 1, n_threads / nv,))
                    t.start()
        else:
            for i in range(0, nv):
                if vertices[i] not in arr:
                    arr1 = copy.deepcopy(arr)
                    arr1[pos] = vertices[i]
                    self.searchParallel(arr1, pos + 1, 1)

    def search(self, arr, pos):
        if self.found:
            return
        if pos >= len(arr):
            if self.graph.is_hamiltonian(arr):
                if not self.found:
                    self.found = True
                    self.solution = arr
                    print(arr)
            return
        vertices = self.graph.getVertices()
        nv = self.graph.getNumberOfVertices()
        for i in range(0, nv):
            if vertices[i] not in arr:
                arr1 = copy.deepcopy(arr)
                arr1[pos] = vertices[i]
                self.search(arr1, pos + 1)
