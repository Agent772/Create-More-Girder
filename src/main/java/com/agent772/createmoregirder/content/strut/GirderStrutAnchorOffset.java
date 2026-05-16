package com.agent772.createmoregirder.content.strut;

import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Computes whether a Girder Strut's anchor should be visually offset 1 px
 * deeper into the supporting neighbor.
 *
 * <p>The offset applies when the strut is attached to the <em>long side</em> of
 * a <em>horizontal</em> girder (a {@link GirderBlock} with {@code X != Z}, or a
 * {@link GirderEncasedShaftBlock} — always horizontal). On a long side, the
 * girder's I-beam silhouette is recessed by 1 px relative to the block bounds,
 * leaving a visible gap between the bracket and the girder. The offset closes
 * that gap by shifting the bracket and the beam start point 1 px toward the
 * neighbor (in {@code -FACING}), where {@code FACING} points away from the
 * supporting block.
 */
public final class GirderStrutAnchorOffset {

    public static final double OFFSET_BLOCKS = 1.0 / 16.0;

    private GirderStrutAnchorOffset() {
    }

    /**
     * @param strutFacing the strut's {@link GirderStrutBlock#FACING} — points
     *                    away from the supporting neighbor.
     */
    public static boolean shouldOffset(BlockGetter level, BlockPos strutPos, Direction strutFacing) {
        if (level == null || strutPos == null || strutFacing == null) {
            return false;
        }
        BlockPos neighborPos = strutPos.relative(strutFacing.getOpposite());
        BlockState neighborState = level.getBlockState(neighborPos);
        return shouldOffsetForNeighbor(neighborState, strutFacing);
    }

    public static boolean shouldOffsetForNeighbor(BlockState neighborState, Direction strutFacing) {
        if (neighborState == null || strutFacing == null) {
            return false;
        }
        Direction.Axis girderAxis = girderHorizontalAxis(neighborState);
        if (girderAxis == null) {
            return false;
        }
        return strutFacing.getAxis() != girderAxis;
    }

    /**
     * Returns the horizontal axis of {@code state} if it is a horizontal girder
     * (or an encased-shaft girder), otherwise {@code null}.
     */
    private static Direction.Axis girderHorizontalAxis(BlockState state) {
        if (state.getBlock() instanceof GirderBlock) {
            boolean x = state.getValue(GirderBlock.X);
            boolean z = state.getValue(GirderBlock.Z);
            if (x == z) {
                return null;
            }
            return x ? Direction.Axis.X : Direction.Axis.Z;
        }
        if (state.getBlock() instanceof GirderEncasedShaftBlock) {
            return state.getValue(GirderEncasedShaftBlock.HORIZONTAL_AXIS);
        }
        return null;
    }
}
