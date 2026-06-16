package me.drex.vanish.util.predicate;

import java.util.Optional;

public interface IEntityFlagsPredicate {
    void vanish$setVisible(Optional<Boolean> vanished);

    Optional<Boolean> vanish$isVisible();
}
