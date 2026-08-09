/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.compat.datafixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

/**
 * Legacy block-id fixer placeholder.
 *
 * Old Forge MissingMappingsEvent based remapping was removed from the 1.21.1
 * NeoForge compile path. The historical mapping table can be restored using
 * the modern registry migration facilities after the main port compiles.
 */
public class BlockIDFixer extends DataFix {
    public BlockIDFixer(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @Override
    public TypeRewriteRule makeRule() {
        return null;
    }
}
