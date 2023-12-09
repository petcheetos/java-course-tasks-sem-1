package edu.project4.render;

import edu.project4.model.Coefficient;
import edu.project4.model.FractalImage;
import edu.project4.model.Pixel;
import edu.project4.model.Point;
import edu.project4.model.Rect;
import edu.project4.transformation.Transformation;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class RenderedMultiThreads implements Renderer {
    private final short start = -20;
    private final ExecutorService executorService;

    public RenderedMultiThreads(int threads) {
        executorService = Executors.newFixedThreadPool(threads);
    }

    @Override
    public FractalImage render(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int samples,
        int iterPerSample,
        int symmetry
    ) {
        Coefficient[] coefficients = new Coefficient[samples];
        for (int i = 0; i < samples; i++) {
            coefficients[i] = Coefficient.init();
        }

        for (int num = 0; num < samples; num++) {
            executorService.execute(() -> executeIterations(
                canvas,
                world,
                variations,
                iterPerSample,
                symmetry,
                coefficients
            ));
        }
        executorService.shutdown();
        return canvas;
    }

    private void executeIterations(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int iterPerSample,
        int symmetry, Coefficient[] coefficients
    ) {
        Point pw = world.getRandomPoint();
        for (short step = start; step < iterPerSample; step++) {
            Coefficient coefficient = coefficients[ThreadLocalRandom.current().nextInt(0, coefficients.length)];
            pw = getPointAffineTransformation(coefficient, pw);
            Transformation transformation =
                variations.get(ThreadLocalRandom.current().nextInt(0, variations.size()));
            pw = transformation.apply(pw);
            if (step >= 0) {
                double theta = 0.0;
                for (int s = 0; s < symmetry; theta += Math.PI * 2 / symmetry, s++) {
                    Point pwr = getRotatedPoint(pw, theta);
                    if (world.contains(pwr)) {
                        Pixel pixel = canvas.getPixel(
                            (int) ((pwr.x() - world.x()) * canvas.getWidth() / world.width()),
                            (int) ((pw.y() - world.y()) * canvas.getHeight() / world.height())
                        );
                        if (pixel == null) {
                            continue;
                        }
                        synchronized (pixel) {
                            Color color = coefficient.color();
                            if (pixel.getHitCount() == 0) {
                                pixel.setColor(coefficient.color().getRed(),
                                    coefficient.color().getGreen(), coefficient.color().getBlue()
                                );
                            } else {
                                int red = (pixel.getColor().getRed() + color.getRed()) / 2;
                                int green = (pixel.getColor().getGreen() + color.getGreen()) / 2;
                                int blue = (pixel.getColor().getBlue() + color.getBlue()) / 2;
                                pixel.setColor(red, green, blue);
                            }
                            pixel.addHit();
                        }
                    }
                }
            }
        }
    }

    private Point getPointAffineTransformation(Coefficient coefficient, Point point) {
        double x = coefficient.a() * point.x() + coefficient.b() * point.y() + coefficient.c();
        double y = coefficient.d() * point.x() + coefficient.e() * point.y() + coefficient.f();
        return new Point(x, y);
    }

    private Point getRotatedPoint(Point point, double theta) {
        double x = point.x() * Math.cos(theta) - point.y() * Math.sin(theta);
        double y = point.x() * Math.sin(theta) + point.y() * Math.cos(theta);
        return new Point(x, y);
    }
}