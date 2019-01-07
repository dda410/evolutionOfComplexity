#!/home/dimitri/anaconda3/bin/python
import numpy as np
import matplotlib.pyplot as plt
import argparse


parser = argparse.ArgumentParser()
parser.add_argument('integers', metavar='N', type=int, nargs='+',
                    help='the frequencies of the bacteria')
args = parser.parse_args()
bacteriaT2 = args.integers.pop()
bacteriaT1 = args.integers.pop()
print(bacteriaT1)
print(bacteriaT2)
bacteria_frequencies = np.array(args.integers[:len(args.integers) // 2])
bacteria_frequencies2 = np.array(args.integers[len(args.integers) // 2:])
#bacteria_frequencies = np.array(args.integers)
columns = 4
# print(bacteria_frequencies.shape)
bacteria_frequencies = bacteria_frequencies.reshape(int(bacteria_frequencies.shape[0] / columns), columns)
bacteria_frequencies2 = bacteria_frequencies2.reshape(int(bacteria_frequencies2.shape[0] / columns), columns)
# print(bacteria_frequencies)
print(bacteria_frequencies)
print(bacteria_frequencies2)
# print(bacteria_frequencies[:, 0])
sl, cl, ss, cs = bacteria_frequencies[:, 0], bacteria_frequencies[:, 1], bacteria_frequencies[:, 2], bacteria_frequencies[:, 3]
plt.plot(sl, '-', label="Selfish Large T{}".format(bacteriaT1))
plt.plot(cl, '-', label='Coop. Large T{}'.format(bacteriaT1))
plt.plot(ss, '-', label='Selfish Small T{}'.format(bacteriaT1))
plt.plot(cs, '-', label='Coop. Small T{}'.format(bacteriaT1))
sl2, cl2, ss2, cs2 = bacteria_frequencies2[:, 0], bacteria_frequencies2[:, 1], bacteria_frequencies2[:, 2], bacteria_frequencies2[:, 3]
plt.plot(sl2, '-', label='Selfish Large T{}'.format(bacteriaT2))
plt.plot(cl2, '-', label='Coop. Large T{}'.format(bacteriaT2))
plt.plot(ss2, '-', label='Selfish Small T{}'.format(bacteriaT2))
plt.plot(cs2, '-', label='Coop. Small T{}'.format(bacteriaT2))
plt.suptitle("Genotype frequencies over generations", fontsize=16)
plt.ylabel('Group Size')
plt.xlabel('Generations')
plt.legend(loc='upper right')
plt.show()
