#!/home/dimitri/anaconda3/bin/python
import numpy as np
import matplotlib.pyplot as plt
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('integers', metavar='N', type=int, nargs='+',
                    help='the frequencies of the bacteria')
args = parser.parse_args()
bacteria_frequencies = np.array(args.integers)
columns = 4
print(bacteria_frequencies.shape)
bacteria_frequencies = bacteria_frequencies.reshape(int(bacteria_frequencies.shape[0] / columns), columns)
print(bacteria_frequencies)
print(bacteria_frequencies[:, 0])
sl, cl, ss, cs = bacteria_frequencies[:, 0], bacteria_frequencies[:, 1], bacteria_frequencies[:, 2], bacteria_frequencies[:, 3]
plt.plot(sl, '-o', label='Selfish Large')
plt.plot(cl, '-o', label='Coop. Large')
plt.plot(ss, '-o', label='Selfish Small')
plt.plot(cs, '-o', label='Coop. Small')
plt.suptitle("Genotype frequencies over generations", fontsize=16)
plt.ylabel('Group Size')
plt.xlabel('Generations')
plt.legend(loc='upper right')
plt.show()
