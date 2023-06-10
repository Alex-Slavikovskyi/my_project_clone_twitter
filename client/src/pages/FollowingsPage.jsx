import { Box } from "@mui/material";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { Followers } from "src/components/Followers/Followers";
import { getFollowings } from "src/redux/thunk/getFollowings";

export const FollowingsPage = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const following = useSelector((state) => state.followings.followings) || [];
  useEffect(() => {
    // if (following.length === 0) {
    dispatch(getFollowings(id));
    //   return;
    // }
  }, [dispatch]);

  return (
    following.content && (
      <Box
        sx={{
          display: "flex",
          direction: "column",
        }}
      >
        <Followers follow={following.content} />
      </Box>
    )
  );
};
