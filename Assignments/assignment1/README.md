# ASSIGNMENT 1

## The details

The assignment specifications can be found 
[here](https://secure.ecs.soton.ac.uk/notes/comp6202/assignment1.pdf).
The assignment is about the implementation from scratch of different
genetic algorithms with the goal of evaluating their performance.
We introduce the following concepts:

- __Mutation__: If we think about an Individual as a string of
	character mutation involves changing some (a percentage) of its
	characters.
- __Tournament selection__: It is a technique to select individuals
	from a population. The size of the tournament means how many random
	individuals are chosen randomly from a population from a
	population. The fittest one is usually chosen as a candidate for
	mutation and the less fittest one for substitution.
- __Crossover__: It is a technique to produce a new individual from
	two different individuals. In uniform crossover each character
	(=gene) has an equal probability to be picked from the first parent
	or from the second parent.
  
## The algorithms

It follows a list of the implemented algorithms with their
description:

#### Basic Hill Climber
This algorithm has just an individual (population size of 1) and uses
mutation. The individual gets mutated and if the mutation increases
its fitness (= it is closer to the goal) than the individual gets
substituted by the mutated one, otherwise does not get
substituted. The algorithm stops when the individual fitness is
maximum (= when the individual is the same as the goal individual).

#### Hill Climber
This algorithm was implemented by mistake but I still decided to keep
it in order to analyze its performance. It works as the basic hill
climber with the difference of having a population. The algorithm
iterates through each member of the population and applies the basic
hill climber algorithm. If at the end of the iteration there is no
maximum fitness among at least one of the individual the iteration
starts again.

#### Genetic algorithm without crossover
The algorithm can be summarized as follows:
	- Pick 2 random individuals -> fitter one will be the parent
	- Mutation stage -> mutate the generated child
	- Pick 2 random individuals -> substitute less fit with the
      generated child.
	  
The algorithm was implemented with a tournament size of 2.

#### Genetic algorithm with crossover
The algorithm can be summarized as follows:
	- Pick 2 Random individuals -> fitter one will be the mother
	- Pick 2 Random individuals -> fitter one will be the father
	- Reproduction stage -> generate child individual from the genes of the parents
	- Mutation stage -> mutate the generated child
	- Pick 2 Random individuals -> substitute less fit wiht the generated child.

The algorithm, again, was implemented with a tournament size of 2 and
uniform crossover.

## Testing

The testing of the algorithms was done with different mutuation rates
and population sizes. Furthermore, the results were averaged on 10
different runs of the same algorithms. The different tested mutation
rates were the following: 3%, 6%, 12% where 3% is approximately 1/L
probability of changing the character. The different tested population
sizes were the following: 10, 20, 50, 100, 200, 500 and 1000
individuals.

## Evaluation

The results outputted by the program can be seen in the
`assignemnt1_output.csv` file in this folder. The results have been
ordered based on the performance of the algorithm (from best to
worst). The performance of the algorithm was measured as the number of
iterations the algorithm had to take before finding the maximum
fitness. What we can immediately see from the output is that the
__crossover algorithm outperforms the others in most situations__. The
best run of the genetic algorithm without crossover takes
approximately twice the iterations to find the maximum fitness
(13308 GA with crossover vs 26913 GA without crossover). We can also
note that, increasing the mutation rate, the GA with crossover
performs poorly when the mutation rate is 12%. This is also true for
the other algorithms. The hill climber algorithm performs really
poorly and this is probably because it is like running N basic hill
climber sequentially, where N is the size of the population. We can ,
in fact, observer that the number of the iterations that the Hill
Climber with population takes is approximately N times the one with
just an individual (when tested with the same parameters). 

Having analyzed the output we can now answer the questions:

1. __If you use a small population is the result the same?
       Explain.__ The GA with crossover performs way faster with a big
       population (500 or 1000 individuals). This is probably because
       the bigger the population the more probability the algorithm
       has to pick two fit parent and make a fitter child out of
       it. This said it is also true that generating a really big
       population can be an expensive operation, this means that the
       right size should be chosen with care. We can also observer
       that in the GA without crossover the algorithm performs best
       with a smaller population(50 or 20) and performs really poorly
       with high sized populations (500 or 1000). This is probably
       because, removed the crossover, a big population does not give
       any advantage to the algorithm.
	   
2. __In this problem any simple optimisation method like these
	will always find the optimum solution sooner or later. If the
	problem was a more difficult optimisation problem, do you think
	the answer to the hypothesis would be the same? Explain.__ When
	the problem is more complex and thus the algorithms are dealing
	with a more computationally complex population and fitness
	function it can absolutely be the case that a not well implemented
	evolutionary algorithm does not end or does not find an optimal
	solution in a reasonable time to be any useful. Even in this
	assignment our poorly designed hill climber with population was
	taking a very long time to find solutions when the mutation rate
	was set to an high value. What I think came out from this
	assignment and the testing phase is that, in order to implement
	working evolutionary algorithm, deep attention has to be put in
	which algorithm, which fitness function and which parameters to
	choose (e.g. mutation rate or population size).
	   
