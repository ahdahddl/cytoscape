package fing.util;

/**
 * A heap can have two states: ordered and unordered.
 */
public final class MinIntHeap
{

  // This must be a non-negative integer.
  private static final int DEFAULT_CAPACITY = 11;

  private int[] m_heap;
  private int m_currentSize;
  private boolean m_orderOK;

  /**
   * A new heap is ordered.
   */
  public MinIntHeap()
  {
    m_heap = new int[DEFAULT_CAPACITY + 1];
    m_heap[0] = Integer.MIN_VALUE;
    m_currentSize = 0;
    m_orderOK = true;
  }

  /**
   * This constructor tosses length elements from the array arr, beginning
   * at index beginIndex, onto a new heap.  The new heap will not be
   * ordered.<p>
   * A copy of the input array is made.  The input array is never modified.
   */
  public MinIntHeap(int[] arr, int beginIndex, int length)
  {
    m_heap = new int[length + DEFAULT_CAPACITY + 1];
    System.arraycopy(arr, 0, m_heap, 1, length);
    m_heap[0] = Integer.MIN_VALUE;
    m_currentSize = length;
    m_orderOK = false;
  }

  /**
   * Returns the number of elements currently in this heap.
   */
  public final int numElements()
  {
    return m_currentSize;
  }

  /**
   * Returns true if and only if the heap is currently ordered.
   */
  public final boolean isOrdered()
  {
    return m_orderOK;
  }

  /**
   * Tosses a new element onto the heap.  The heap will be become
   * unordered after this operation; this operation takes constant time.
   */
  public final void toss(int x)
  {
    checkSize();
    m_heap[++m_currentSize] = x;
    m_orderOK = false;
  }

  /**
   * If this heap is ordered prior to calling this operation, adds
   * specified element to heap such that the heap will remain ordered after
   * this operation, taking O(log(n)) time where n is the number of
   * elements in this heap (average time is actually constant regardless of
   * size of heap).  If this heap is not ordered when this operation is called,
   * adds specified element to heap in constant time.
   */
  public final void insert(int x)
  {
    checkSize();
    m_heap[++m_currentSize] = x;
    if (m_orderOK) percolateUp(m_heap, m_currentSize);
  }

  /**
   * Returns the minimum element in this heap.  This is a constant time
   * operation if the heap is ordered.  If the heap is not ordered, this
   * operation will first order the entire heap.  The time complexity of
   * ordering an unordered heap is O(n) where n is the number of elements
   * in the heap.  This method leaves the heap in an ordered state.<p>
   * If there are no elements in this heap, results of this operation
   * are undefined.
   * @see #numElements()
   */
  public final int findMin()
  {
    if (!m_orderOK) { // Fix heap.
      for (int i = m_currentSize / 2; i > 0; i--)
        percolateDown(m_heap, i, m_currentSize);
      m_orderOK = true; }
    return m_heap[1];
  }

  /**
   * Deletes and returns the minimum element in this heap.  This operation
   * has time complexity O(log(n)) where n is the number of elements
   * currently in this heap, assuming that the heap is ordered.  If the
   * heap is not ordered at the time this operation is invoked, this
   * operation will first order the entire heap.  The time complexity of
   * ordering an unordered heap is O(n), where n is the number of elements
   * in the heap.  When this method returns, this heap is in an ordered
   * state.<p>
   * If there are no elements in this heap, results of this operation
   * are undefined.
   * @see #numElements()
   */
  public final int deleteMin()
  {
    if (!m_orderOK) { // Fix heap.
      for (int i = m_currentSize / 2; i > 0; i--)
        percolateDown(m_heap, i, m_currentSize);
      m_orderOK = true; }
    final int returnThis = m_heap[1];
    m_heap[1] = m_heap[m_currentSize--];
    percolateDown(m_heap, 1, m_currentSize);
    return returnThis;
  }

  private final void checkSize()
  {
    if (m_currentSize < m_heap.length - 1) return;
    final int[] newHeap = new int[m_heap.length * 2 + 1];
    System.arraycopy(m_heap, 0, newHeap, 0, m_heap.length);
    m_heap = newHeap;
  }

  private static final void percolateUp(int[] heap,
                                        int childIndex)
  {
    for (int parentIndex = childIndex / 2;
         heap[childIndex] < heap[parentIndex];
         childIndex = parentIndex, parentIndex = parentIndex / 2)
      swap(heap, parentIndex, childIndex);
  }

  private static final void percolateDown(int[] heap,
                                          int parentIndex,
                                          int size)
  {
    for (int childIndex = parentIndex * 2; childIndex <= size;
         parentIndex = childIndex, childIndex = childIndex * 2) {
      if (childIndex + 1 <= size && heap[childIndex + 1] < heap[childIndex])
        childIndex++;
      if (heap[childIndex] < heap[parentIndex])
        swap(heap, parentIndex, childIndex);
      else break; }
  }

  private static final void swap(int[] arr, int index1, int index2)
  {
    int temp = arr[index1];
    arr[index1] = arr[index2];
    arr[index2] = temp;
  }

  /**
   * Returns an iterator of elements in this heap, ordered such that
   * the least element is first in the returned iterator.  Pruning of
   * duplicate elements is enabled by setting pruneDuplicates to true.<p>
   * If pruneDuplicates is false, this method returns in constant
   * time (unless this heap is unordered when this method is called, in
   * which case this method returns in O(n) time), and the returned iterator
   * takes O(log(n)) time complexity to return each successive element.
   * If pruneDuplicates is true, this method takes O(n*log(n)) time
   * complexity to come up with the return value, and
   * the retuned iterator takes constant time to return each successive
   * element.<p>
   * The returned iterator becomes "invalid" as soon as any other method
   * on this heap instance is called; calling methods on an invalid iterator
   * will cause undefined behavior in both the iterator and in the underlying
   * heap.<p>
   * Calling this function automatically causes this heap to become
   * unordered.  No elements are added or removed from this heap as a
   * result of using the returned iterator.
   * @see #elements()
   */
  public final IntIterator orderedElements(boolean pruneDuplicates)
  {
    final int[] heap = m_heap;
    final int size = m_currentSize;
    if (!m_orderOK) // Fix heap.
      for (int i = size / 2; i > 0; i--) percolateDown(heap, i, size);
    m_orderOK = false; // That's right - the heap becomes unordered.
    if (pruneDuplicates)
    {
      int dups = 0;
      int sizeIter = size;
      while (sizeIter > 1) { // Needs to be 1, not 0, for duplicates.
        swap(heap, 1, sizeIter);
        percolateDown(heap, 1, sizeIter - 1);
        if (heap[1] == heap[sizeIter]) dups++;
        sizeIter--; }
      final int numDuplicates = dups;
      return new IntIterator() {
          int m_index = size;
          int m_dups = numDuplicates;
          int m_prevValue = heap[m_index] + 1;
          public int numRemaining() { return m_index - m_dups; }
          public int nextInt() {
            while (heap[m_index] == m_prevValue) {
              m_dups--; m_index--; }
            m_prevValue = heap[m_index--];
            return m_prevValue; } };
    }
    else // Don't prune duplicates.  Do lazy computation.
    {
      return new IntIterator() {
          int m_size = size;
          public int numRemaining() { return m_size; }
          public int nextInt() {
            swap(heap, 1, m_size);
            percolateDown(heap, 1, m_size - 1);
            return heap[m_size--]; } };
    }
  }

  /**
   * This method has the exact same behavior as orderedElements(boolean),
   * only that it returns an array instead of an iterator.  The returned
   * array can be safely modified without having an effect on the
   * underlying heap (that is, the returned array is not an internal
   * data structure of this heap; a new array is instantiated every time this
   * method is called).
   * @see #orderedElements(boolean)
   * @deprecated This method is here to test the performance of this heap
   *   with all methods inlined; this method has been deprecated ever since
   *   it was first written, and it will be taken out of this class definition
   *   at liberty.
   */
  public final int[] _orderedElements(boolean pruneDuplicates)
  {
    final int[] heap = m_heap;
    final int size = m_currentSize;
    int parentIndex;
    int childIndex;
    int temp;
    if (!m_orderOK) // Fix heap.
      for (int i = size / 2; i > 0; i--) { // Percolate down.
        parentIndex = i;
        for (childIndex = parentIndex * 2; childIndex <= size;
             parentIndex = childIndex, childIndex = childIndex * 2) {
          if (childIndex + 1 <= size &&
              heap[childIndex + 1] < heap[childIndex])
            childIndex++;
          if (heap[childIndex] < heap[parentIndex]) { // Swap.
            temp = heap[parentIndex];
            heap[parentIndex] = heap[childIndex];
            heap[childIndex] = temp; }
          else break; } }
    m_orderOK = false; // That's right - the heap becomes unordered.
    if (pruneDuplicates)
    {
      int dups = 0;
      int sizeIter = size;
      while (sizeIter > 1) { // Needs to be 1, not 0, for duplicates.
        temp = heap[1];
        heap[1] = heap[sizeIter];
        heap[sizeIter] = temp;
        parentIndex = 1;
        for (childIndex = parentIndex * 2; childIndex <= sizeIter - 1;
             parentIndex = childIndex, childIndex = childIndex * 2) {
          if (childIndex + 1 <= sizeIter - 1 &&
              heap[childIndex + 1] < heap[childIndex])
            childIndex++;
          if (heap[childIndex] < heap[parentIndex]) {
            temp = heap[parentIndex];
            heap[parentIndex] = heap[childIndex];
            heap[childIndex] = temp; }
          else break; }
        if (heap[1] == heap[sizeIter]) dups++;
        sizeIter--; }
      final int[] returnThis = new int[size - dups];
      final int length = returnThis.length;
      int index = size;
      int prevValue = heap[index] + 1;
      for (int i = 0; i < length; i++) {
        while (heap[index] == prevValue) index--;
        prevValue = heap[index--];
        returnThis[i] = prevValue; }
      return returnThis;
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns an iteration over all the elements currently in this heap;
   * the order of elements in the returned iteration is undefined.<p>
   * If other methods in this heap are called while iterating through
   * the return value, behavior of the iterator is undefined.<p>
   * This iterator has no effect on the set of element in this heap.  This
   * iterator has no effect on the ordered state of this heap.
   * @see #orderedElements(boolean)
   */
  public final IntIterator elements()
  {
    final int[] heap = m_heap;
    final int size = m_currentSize;
    return new IntIterator() {
        int index = 0;
        public int numRemaining() { return size - index; }
        public int nextInt() { return heap[++index]; } };
  }

}
