package com.aim.meteors.flows;

import static com.aim.meteors.flows.Flow.FlowPhase.Active;
import static com.aim.meteors.flows.Flow.FlowPhase.Finished;
import static com.aim.meteors.flows.Flow.FlowPhase.Idle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Flow<Out> implements Iterator<Out>, Iterable<Out>

{
  // ======================================================================
  // Types
  // ======================================================================
  public enum FlowPhase {
    Idle, Active, Finished
  }

  // ======================================================================
  // Fields
  // ======================================================================
  /*----- working state -----*/
  /* This stream's lifecycle phase. */
  protected FlowPhase phase = Idle;

  /* The next item to be returned. */
  protected Out next;

  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public Flow() {}

  public Flow(Out value) {
    next = value;
  }

  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Property Access
  // ---------------------------
  public Iterator<Out> iterator() {
    return this;
  }

  // ---------------------------
  // Lifecycle Management
  // ---------------------------
  /**
   * Performs any needed work upon completion of a stream.
   */
  protected void doFlowFinishingAction() {
    next = null;
  }

  /**
   * Performs any needed work at start of a stream.
   */
  public void doFlowStartingAction() {
    phase = Active;
    findNext();
  }

  public void finished() {
    if(phase != Finished) {
      phase = Finished;
      doFlowFinishingAction();
    }
  }

  /**
   * Prepares this stream to access the <code>store</code>.
   */
  public Flow<Out> on() {
    return this;
  }

  /**
   * Resets the state.
   */
  public void reset() {
    phase = Idle;
  }

  public Flow<Out> beStable() {
    return this;
  }

  // ---------------------------
  // Flow Operations
  // ---------------------------
  // public void accept(List<Out> tokens) {
  // super.accept(tokens);
  // // TODO Auto-generated method stub
  // }
  //
  /**
   * Returns the currently-selected value.
   */
  public Out current() {
    return next;
  }

  /**
   * Finds the next value in this stream. This method should be overridden.
   */
  protected void findNext() {
    throw new UnsupportedOperationException("must be overridded");
  }

  /**
   * Returns the source's first item, or null if empty. In the case of sets, the concept of "first"
   * refers to hash order, so is essentially random.
   */
  public synchronized Out first() {
    reset(); // rewind
    hasNext();
    return next;
  }

  /**
   * Returns whether {@Binding #current current} will return a non-null value.
   */
  public boolean hasCurrent() {
    return next != null;
  }

  /**
   * Returns whether this stream has a next item.
   */
  public boolean hasNext() {
    switch(phase) {
      case Idle:
        doFlowStartingAction();
        // fall through

      case Active:
        if(next != null) {
          return true;
        } else {
          finished();
          return false;
        }

      case Finished:
        return false;
    }
    return false;
  }

//  public <R> Flow<R> map(Function<? super Out, ? extends R> mapper) {
//    return new FlowMapper2(this, mapper);
//  }

  /**
   * Returns the next item if available, else throws EndOfStreamException.
   */
  public Out next() {
    switch(phase) {
      case Idle:
        doFlowStartingAction();
        // fall through

      case Active:
        Out temp = next;
        findNext();
        return temp;

      case Finished:
        throw new RuntimeException("off end of stream");
    }
    return null;
  }

  // ---------------------------
  // Stream Operations
  // ---------------------------
  /**
   * Performs <code>action</code> for each item of this stream, until a returnTrap
   * is thrown, terminating the method.
   */
  public void forEach(Consumer<? super Out> action) {
    while(hasNext())
      action.accept(next());
  }

  // ---------------------------
  // Push Operations
  // ---------------------------
  /**
   * Drains all this source's (remaining) items into <code>collection</code>.
   */
  public void drainInto(Collection<Out> collection) {
    forEach(item -> collection.add(item));
  }

  // ---------------------------
  // Conversion And Collection
  // ---------------------------
  /**
   * Returns a list containing this source's items, consuming them.
   */
  public List<Out> toList() {
    List<Out> list = new ArrayList();
    drainInto(list);
    return list;
  }

  /**
   * Returns a set containing this source's items, consuming them. The returned
   * set is an <code>IdentitySet</code> if the outType is an object type, i.e. whose
   * members have references, else is an <code>EqualitySet</code>.
   */
  public Set<Out> toSet() {
    Set<Out> set = new HashSet();
    drainInto(set);
    return set;
  }

  protected void trap() {
    // breakpoint here
  }
}
