package pipesag.io;

import pipesag.datastructure.CuttingProcess;
import pipesag.exceptions.FileAccessException;
import pipesag.framework.Consumable;
import pipesag.framework.Handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@code FileConsumer} class is responsible for consuming processed data
 * from a queue and writing it to output files. It implements the {@code Consumable}
 * and {@code Runnable} interfaces, allowing it to handle data writing and run
 * as a thread.
 *
 * <p>This class processes data objects containing an {@link OrderJob} (input data)
 * and a {@link CuttingProcess} (output data) and writes the results to files.
 * Files are saved in the same directory as the input files, with appropriate
 * extensions based on whether the operation was successful or contained errors.</p>
 *
 * <p>The class is designed to run continuously, processing data from the queue
 * until a termination signal is detected (an {@code OrderJob} with {@code null}
 * input).</p>
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Processes data objects and writes the results to output files.</li>
 *   <li>Handles errors gracefully by appending an {@code .err} extension for error files.</li>
 *   <li>Uses a {@link LinkedBlockingQueue} to handle thread-safe data transfer.</li>
 *   <li>Implements a polling mechanism to process data from the queue until termination.</li>
 * </ul>
 *
 * @ <{@link pipesag.framework.Handler.Data}<{@code OrderJob, CuttingProcess}>> represents the data structure
 * containing input and output objects.
 * @see Consumable
 * @see Runnable
 */
public class FileConsumer implements Consumable<Handler.Data<OrderJob, CuttingProcess>>, Runnable {

    private final LinkedBlockingQueue<Handler.Data<OrderJob, CuttingProcess>> resultQueue;

    /**
     * Constructs a {@code FileConsumer} with the specified result queue.
     *
     * @param resultQueue the queue containing data to be processed and written to files
     */
    public FileConsumer(LinkedBlockingQueue<Handler.Data<OrderJob, CuttingProcess>> resultQueue) {
        this.resultQueue = resultQueue;
    }

    /**
     * Writes the processed data to an output file in the same directory as the input file.
     *
     * <p>The method generates the target file path based on the input file's path:
     * <ul>
     *   <li>If the operation is successful, the file is saved with an {@code .out} extension.</li>
     *   <li>If there are errors, the file is saved with an {@code .err} extension,
     *   containing error messages.</li>
     * </ul></p>
     *
     * @param data the data object containing the input and output objects
     * @throws FileAccessException if there is an issue writing the file to disk
     * @throws InputMismatchException if the input file path lacks an extension
     */
    @Override
    public void write(Handler.Data<OrderJob, CuttingProcess> data) throws FileAccessException {
        OrderJob input = data.in;
        CuttingProcess output = data.out;

        // The output files will be saved in the same directory where input file/s is/are.
        String inputPath = input.getFilepath();
        if (inputPath.indexOf(".") <= 0) {
            throw new InputMismatchException("The input data doesn't contain any extension.");
        }
        int lastIndex = inputPath.lastIndexOf(File.separator) + 1;
        String targetPath = inputPath.substring(0, lastIndex);
        targetPath += "optimized_";
        targetPath+= inputPath.substring(lastIndex, inputPath.lastIndexOf("."));

        StringBuilder stb = new StringBuilder();
        if (output.cuttings().isEmpty()) {
            targetPath += ".err";
            stb.append(input.getError());
        } else {
            targetPath += ".out";
            stb.append(input.getOrder().toString()).append(System.lineSeparator());
            stb.append(System.lineSeparator()).append(output.toString());
        }

        Path outputPath = Path.of(targetPath);
        try (BufferedWriter bfw = Files.newBufferedWriter(outputPath)) {
            bfw.write(stb.toString());
        } catch (IOException e) {
            throw new FileAccessException("Could not write output file to path: " + outputPath.toAbsolutePath(), e);
        }

    }

    /**
     * Continuously consumes data from the queue and writes it to output files.
     *
     * <p>The method runs in an infinite loop, polling the queue for data. If the
     * queue contains a termination signal (an object with {@code null} input),
     * the loop terminates. Any file access errors are logged to {@code System.err}.</p>
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (resultQueue.peek() == null) {
                    continue;
                }
                Handler.Data<OrderJob, CuttingProcess> data = resultQueue.poll();
                if (data.in == null) {
                    break;
                }
                this.write(data);
            } catch (FileAccessException fe) {
                System.err.println(fe.getMessage());
            }
        }
    }
}
