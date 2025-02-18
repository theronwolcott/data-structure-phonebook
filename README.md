# Advanced Hash Table Implementations

This project implements **Ordered Linear Probing (OLP)** and **Quadratic Probing (QP)** as advanced collision resolution techniques for open-addressed hash tables. These methods enhance search efficiency and manage clustering more effectively.

## Features

- **Ordered Linear Probing (OLP):**
  - Maintains ordered collision chains to ensure faster failure for unsuccessful searches.
  - Efficiently handles key insertion while preserving searchability.

- **Quadratic Probing (QP):**
  - Uses a quadratic step function to reduce clustering.
  - Provides a balanced dispersion of keys in the hash table.

- **Soft and Hard Deletion:**
  - Implements tombstone markers for soft deletion to maintain collision chain integrity.
  - Hard deletion reinserts keys to maintain hash table structure.

- **Resizing Policies:**
  - Automatically resizes the hash table when load exceeds 50%.
  - Utilizes prime number capacities for optimal hash distribution.
 
# Usage

## Ordered Linear Probing
The `OrderedLinearProbingHashTable` class implements the OLP technique. Keys are inserted while maintaining order in collision chains.

```java
OrderedLinearProbingHashTable<String, Integer> olpTable = new OrderedLinearProbingHashTable<>(false);
olpTable.put("entropy", 42);
olpTable.put("gorilla", 27);
```

## Quadtratic Probing
The 'QuadraticProbingHashTable' class implements QP, using a quadratic function to resolve collisions.
```java
QuadraticProbingHashTable<String, Integer> qpTable = new QuadraticProbingHashTable<>(true);
qpTable.put("entropy", 42);
qpTable.put("gorilla", 27);
```

## Soft Deletion
Both hash table implementations support soft deletion using a tombstone marker.
```java
olpTable.remove("entropy"); // Soft delete
qpTable.remove("gorilla"); // Soft delete
```

## Resizing
The hash tables automatically resize when the load factor exceeds 50%. Capacity is adjusted to the next prime number.

# Performance
Both OLP and QP are evaluated for:

  - Collision resolution efficiency.
  - Search operation speed (successful and unsuccessful).
  - Impact of soft vs. hard deletion.

# Trade-offs
| Feature             | Ordered Linear Probing | Quadratic Probing |
|---------------------|------------------------|-------------------|
| Clustering          | Moderate               | Low               |
| Cache Locality      | High                   | Moderate          |
| Deletion Complexity | Simple (Soft)          | Reinsertion Needed|

