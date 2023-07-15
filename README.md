<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/joszamama/xatkit-spl-parser"></a>
  <h3 align="center">Minimum Weighted Dominating Set Problem</h3>
  <p align="center">
    An iterated greedy algorithm for finding the minimum dominating set in weighted graphs 
    <br />
    <br />
    <a href="https://github.com/joszamama/weighted-dominating-set/issues">Report Bug</a>
    ·
    <a href="https://github.com/joszamama/weighted-dominating-set/issues">Request Feature</a>
  </p>
</div>

<!-- ABOUT THE PROJECT -->
## About The Project

A dominating set in a weighted graph is a set of vertices such that every vertex outside the set is adjacent to a vertex in the set, and the sum of edge weights incident to the set for every vertex is at least K. The domination number is the minimum cardinality of a dominating set in the graph. The problem of finding the minimum weighted dominating set is a combinatorial optimization problem that has been proved to be N P-hard. Given the difficulty of this problem, an Iterated Greedy algorithm is proposed for its solution and it is compared to the solution given by an exact algorithm and by the state-of-art algorithms. Computational results show that the proposal is able to find optimal or near-optimal solutions within a short computational time.

<p align="right">(<a href="#readme.md-top">back to top</a>)</p>

### Built With

* [Java](https://www.java.com/es/)
* [Maven](https://maven.apache.org/)

<p align="right">(<a href="#readme.md-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

To run and test this project locally, you must have Maven, Java 19 and Git installed.

### Installation

1. Clone the repo:
   ```sh
   git clone https://github.com/joszamama/dominating-set.git
   ```
2. Install the Maven dependencies:
   ```sh
   cd dominating-set
   mvn clean compile
   ```

<p align="right">(<a href="#readme.md-top">back to top</a>)</p>


<!-- LICENSE -->
## License

Distributed under the EPL-2.0 license. See `LICENSE.md` for more information.

<p align="right">(<a href="#readme.md-top">back to top</a>)</p>
