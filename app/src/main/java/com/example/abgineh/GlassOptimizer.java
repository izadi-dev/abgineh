package com.example.abgineh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GlassOptimizer {

    final int sheetWidth;
    final int sheetHeight;

    public GlassOptimizer(int sheetWidth, int sheetHeight) {
        this.sheetWidth = sheetWidth;
        this.sheetHeight = sheetHeight;
    }

    public List<List<Placement>> optimize(List<GlassPiece> pieces) {

        List<GlassPiece> remaining = new ArrayList<>();

        for (GlassPiece p : pieces) {
            for (int i = 0; i < p.quantity; i++) {
                remaining.add(new GlassPiece(
                        p.width,
                        p.height,
                        1
                ));
            }
        }

        remaining.sort(new Comparator<GlassPiece>() {
            @Override
            public int compare(
                    GlassPiece a,
                    GlassPiece b
            ) {
                return (b.width * b.height)
                        - (a.width * a.height);
            }
        });

        List<List<Placement>> resultSheets =
                new ArrayList<>();

        while (!remaining.isEmpty()) {

            List<Placement> placements =
                    new ArrayList<>();

            List<FreeRect> freeRects =
                    new ArrayList<>();

            freeRects.add(
                    new FreeRect(
                            0,
                            0,
                            sheetWidth,
                            sheetHeight
                    )
            );

            Iterator<GlassPiece> iterator =
                    remaining.iterator();

            while (iterator.hasNext()) {

                GlassPiece piece = iterator.next();

                int rectIndex = -1;
                boolean rotated = false;

                for (int i = 0; i < freeRects.size(); i++) {

                    FreeRect r = freeRects.get(i);

                    // حالت عادی
                    if (piece.width <= r.width &&
                            piece.height <= r.height) {

                        rectIndex = i;
                        rotated = false;
                        break;
                    }

                    // حالت چرخش
                    if (piece.height <= r.width &&
                            piece.width <= r.height) {

                        rectIndex = i;
                        rotated = true;
                        break;
                    }
                }

                if (rectIndex != -1) {

                    FreeRect free =
                            freeRects.remove(rectIndex);

                    int w = rotated
                            ? piece.height
                            : piece.width;

                    int h = rotated
                            ? piece.width
                            : piece.height;

                    Placement placement =
                            new Placement(
                                    free.x,
                                    free.y,
                                    w,
                                    h,
                                    rotated
                            );

                    placements.add(placement);

                    // فضای سمت راست
                    FreeRect rightRect =
                            new FreeRect(
                                    free.x + w,
                                    free.y,
                                    free.width - w,
                                    h
                            );

                    // فضای پایین
                    FreeRect bottomRect =
                            new FreeRect(
                                    free.x,
                                    free.y + h,
                                    free.width,
                                    free.height - h
                            );

                    if (rightRect.width > 0 &&
                            rightRect.height > 0) {

                        freeRects.add(rightRect);
                    }

                    if (bottomRect.width > 0 &&
                            bottomRect.height > 0) {

                        freeRects.add(bottomRect);
                    }

                    iterator.remove();
                }
            }

            resultSheets.add(placements);
        }

        return resultSheets;
    }

    public double calculateWaste(
            List<Placement> placements
    ) {

        int usedArea = 0;

        for (Placement p : placements) {
            usedArea += p.width * p.height;
        }

        int totalArea =
                sheetWidth * sheetHeight;

        return 100.0 *
                (totalArea - usedArea)
                / totalArea;
    }
}