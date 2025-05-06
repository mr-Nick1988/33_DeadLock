package telran;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockAppl {
    private static final Lock quantumProcessor = new ReentrantLock();
    private static final Lock neuralNetwork = new ReentrantLock();
    private static final Lock energyCore = new ReentrantLock();
    private static final String[] lockNames = {"Quantum Processor", "Neural Net", "Energy Core"};

    public static void main(String[] args) {
        Thread quantumBot = new Thread(() ->
                executeTask(quantumProcessor, neuralNetwork), "Quant");

        Thread neuronBot = new Thread(() ->
                executeTask(neuralNetwork, energyCore), "Neuron");

        Thread pulsarBot = new Thread(() ->
                executeTask(energyCore, quantumProcessor), "Pulsar");

        quantumBot.start();
        neuronBot.start();
        pulsarBot.start();
    }

    private static void executeTask(Lock first, Lock second) {
        String botName = Thread.currentThread().getName();
        System.out.println(botName + ": CAPTURED " + getLockName(first));
        first.lock();
        try {
            Thread.sleep(100);
            System.out.println(botName + ": WAIT " + getLockName(second));
            second.lock();
            try {
                System.out.println(botName + ": CAPTURED " + getLockName(second));
            } finally {
                second.lock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            first.unlock();
        }
    }

    private static String getLockName(Lock lock) {
        if (lock == quantumProcessor) return lockNames[0];
        if (lock == neuralNetwork) return lockNames[1];
        return lockNames[2];
    }
}
