'''
Created on Feb 28, 2018

@author: ema
'''
from random import randrange
import time


from Graph.DictGraph import DictGraph
from algorithm.Algorithm import Algorithm


class UI:
    def __init__(self):
        pass
    
    @staticmethod
    def printMenu():
        s = '\t\tMenu\n'
        s += '\t1 - Read file\n'
        s += '\t2 - Get the number of vertices\n'
        s += '\t3 - Find if (x, y) is an edge.\n'
        s += '\t4 - Get the in and out degree of a vertex\n'
        s += '\t5 - Show the set of outbound edges of a vertex\n'
        s += '\t6 - Show the set of inbound edges of a vertex\n'
        s += '\t7 - Get the cost of an edge\n'
        s += '\t8 - Modify the cost of an edge\n'
        s += '\t9 - Add edge\n'
        s += '\t10 - Remove edge\n'
        s += '\t11 - Add vertex\n'
        s += '\t12 - Remove vertex\n'
        s += '\t0 - Exit\n'
        print(s)

    def read_graph(self):
        with open("../graph.csv", "r") as f:
            info = f.readline()
            info = info.strip()
            info = info.split(" ")
            n = int(info[0].strip())
            self.graph = DictGraph(n)
            m = int(info[1].strip())
            for i in range(0, m):
                line = f.readline().strip()
                line = line.split(" ")
                x = int(line[0].strip())
                y = int(line[1].strip())
                c = int(line[2].strip())
                self.graph.addEdge(x, y, c)


    def read_vertex(self):
        while True:
            v = input("Enter vertex: ")
            try:
                v = int(v)
                if self.graph.validVertex(v):
                    return v
                else:
                    print("Invalid vertex! Try again.")
            except ValueError:
                print("The vertex must be an integer number. Please try again: ")
                
    def generate_edges(self):
        nr = self.graph.getNumberOfVertices()
        for i in range(0, nr*5):
            y = randrange(nr)
            print(str(i%nr) + " " + str(y) + " " + str(1))
            self.graph.addEdge(i%nr, y, 1)

    def find_hamiltonian(self):
        n = 10
        # self.graph = DictGraph(n)
        # self.generate_edges()
        alg = Algorithm(self.graph, pow(n, 3))
        start = time.time()
        alg.startParallel()
        end = time.time()
        print(end - start)

        alg1 = Algorithm(self.graph, n)
        start = time.time()
        alg1.start()
        end = time.time()
        print(end - start)


    def start(self):
       
        while True:
            UI.printMenu()
            cmd = input("Choose an option: ")
            if cmd == '1':
                self.read_graph()
            elif cmd == '2':
                print(self.graph.getNumberOfVertices())
            elif cmd == '3':
                x = self.read_vertex()
                y = self.read_vertex()
                if self.graph.isEdge(x, y):
                    print("Yes.")
                else:
                    print("No.")
                
            elif cmd == '4':
                v = self.read_vertex()
                i = self.graph.getInDegree(v)
                o = self.graph.getOutDegree(v)
                print("The in degree is: " + str( i ) + " and the out degree is: " + str(o))
            elif cmd == '5':
                v = self.read_vertex()
                for t in self.graph.parseOut(v):
                    print("("  + str(v) + "," + str(t)+")"+ " cost: " + str(self.graph.getCost(v,t)))
            elif cmd == '6':
                v = self.read_vertex()
                for t in self.graph.parseIn(v):
                    print("("  + str(t) + "," + str(v)+")"+ " cost: " + str(self.graph.getCost(t,v)))
            elif cmd == '7':
                x = self.read_vertex()
                y = self.read_vertex()
                if not self.graph.isEdge(x, y):
                    print("No such edge in the graph.")
                else:
                    print("The cost is: " + str(self.graph.getCost(x, y)))
                    
            elif cmd == '8':
                x = self.read_vertex()
                y = self.read_vertex()
                if not self.graph.isEdge(x, y):
                    print("No such edge in the graph.")
                else:
                    while True:
                        c = input("Enter cost: ")
                        try:
                            c = int(c)
                            self.graph.setCost(x, y, c)
                            break
                        except ValueError:
                            print("Cost must be an integer. Try again!")
                    
            elif cmd == '9':
                x = self.read_vertex()
                y = self.read_vertex()
                while True:
                    c = input("Enter cost: ")
                    try:
                        c = int(c)
                        break
                    except ValueError:
                        print("Cost must be an integer. Try again!")
                if not self.graph.isEdge(x, y):
                    self.graph.addEdge(x, y, c)
                
            elif cmd == '10':
                x = self.read_vertex()
                y = self.read_vertex()
                if not self.graph.removeEdge(x, y):
                    print("The edge doesn't exist. ")
            
            elif cmd == '11':
                x = input("Enter vertex: ")
                while True:
                    try:
                        x = int(x)
                        break
                    except ValueError:
                        print("Vertex must be an integer! Try again. ")
                if not self.graph.addVertex(x):
                    print("The vertex is already in the graph. ")
            elif cmd == '12':
                x = self.read_vertex()
                self.graph.removeVertex(x)
            elif cmd == '13':
                self.find_hamiltonian()
            elif cmd == '0':
                
                return False
            else:
                print("Invalid option. Try again!")


ui = UI()
ui.start()