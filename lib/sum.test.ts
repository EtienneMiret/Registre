import { describe, it } from '@jest/globals';
import { sum } from './sum';

describe('sum', () => {

  it.each([
    {a: 2, b: 2, expected: 4},
    {a: 5, b: 3, expected: 8},
    {a: 6, b: 1, expected: 7},
  ])('should find $expected for $a and $b', ({a, b, expected}) => {
    expect(sum(a, b)).toBe(expected);
  });

});
